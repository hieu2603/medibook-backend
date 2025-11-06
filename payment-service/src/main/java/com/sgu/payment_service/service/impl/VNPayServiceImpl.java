package com.sgu.payment_service.service.impl;

import com.sgu.payment_service.config.VnpayConfig;
import com.sgu.payment_service.dto.request.VNPayPaymentRequest;
import com.sgu.payment_service.dto.message.BalanceUpdateMessage;
import com.sgu.payment_service.dto.message.NotificationMessage;
import com.sgu.payment_service.dto.request.VNPayCallbackRequest;
import com.sgu.payment_service.dto.response.payment.VNPayPaymentResponse;
import com.sgu.payment_service.dto.response.payment.PaymentResponse;
import com.sgu.payment_service.enums.BalanceOperation;
import com.sgu.payment_service.enums.PaymentStatus;
import com.sgu.payment_service.enums.PaymentType;
import com.sgu.payment_service.exception.PaymentException;
import com.sgu.payment_service.model.Payment;
import com.sgu.payment_service.repository.PaymentRepository;
import com.sgu.payment_service.service.MessagePublisher;
import com.sgu.payment_service.service.VNPayService;
import com.sgu.payment_service.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URLEncoder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    private final VnpayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final MessagePublisher messagePublisher;

    @Override
    @Transactional

    public VNPayPaymentResponse createPayment(VNPayPaymentRequest request, HttpServletRequest httpRequest) {
        try {
            log.info("Creating VNPay payment for user: {}", request.getUserId());

         
            String paymentId = UUID.randomUUID().toString();

            // Create payment record with PENDING status
            Payment payment = Payment.builder()
                    .id(paymentId) 
                    .userId(request.getUserId())
                    .amount(request.getAmount())
                    .paymentType(PaymentType.DEPOSIT)
                    .status(PaymentStatus.PENDING)
                    .paymentMethod("VNPAY")
                    .description(request.getDescription())
                    .createdAt(LocalDateTime.now()) 
                    .updatedAt(LocalDateTime.now()) 
                    .build();

            
            paymentRepository.save(payment);

            // Build VNPay payment URL
            String vnpayUrl = buildPaymentUrl(payment, request, httpRequest);

            return VNPayPaymentResponse.builder()
                    .code("00")
                    .message("Success")
                    .paymentUrl(vnpayUrl)
                    .build();

        } catch (Exception e) {
            log.error("Error creating VNPay payment", e);
            throw new PaymentException("Không thể tạo thanh toán VNPay: " + e.getMessage());
        }
    }

   @Override
@Transactional
public PaymentResponse handleCallback(Map<String, String> params) {
    try {
        log.info(" Processing VNPay callback");
        
        // Verify signature
        if (!VNPayUtil.validateResponse(params, vnPayConfig.getHashSecret())) {
            log.error(" Invalid VNPay signature");
            throw new RuntimeException("Sai chữ ký VNPAY");
        }
        log.info(" VNPay signature verified successfully");

        // Parse callback
        VNPayCallbackRequest callback = VNPayCallbackRequest.fromMap(params);

        String paymentId = callback.getVnp_TxnRef();
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Không tìm thấy giao dịch"));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            log.warn(" Payment already processed: {}", paymentId);
            return mapToPaymentResponse(payment);
        }

        String responseCode = callback.getVnp_ResponseCode();
        String transactionStatus = callback.getVnp_TransactionStatus();
        String transactionNo = callback.getVnp_TransactionNo();

        log.info(" Payment {}: ResponseCode={}, TransactionStatus={}, TransactionNo={}", 
                paymentId, responseCode, transactionStatus, transactionNo);

        // Xác định success
        boolean isSuccess;
        if (responseCode != null && !responseCode.isEmpty()) {
            isSuccess = "00".equals(responseCode) && "00".equals(transactionStatus);
        } else {
            isSuccess = "00".equals(transactionStatus);
        }

        if (isSuccess) {
            // SUCCESS
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId(transactionNo);
            payment.setUpdatedAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);

            // GỬI MESSAGE ĐẾN USER SERVICE - Cộng tiền
            BalanceUpdateMessage balanceMessage = BalanceUpdateMessage.builder()
                    .userId(payment.getUserId())
                    .amount(payment.getAmount())
                    .operation(BalanceOperation.INCREASE.name())
                    .build();
            messagePublisher.publishBalanceUpdate(balanceMessage);

            // GỬI MESSAGE ĐẾN NOTIFICATION SERVICE
            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .userId(payment.getUserId())
                    .title("Nạp tiền thành công")
                    .message(String.format("Bạn đã nạp thành công %s VNĐ vào tài khoản qua VNPay",
                            String.format("%,d", payment.getAmount().intValue())))
                    .type("DEPOSIT_SUCCESS")
                    .build();
            messagePublisher.publishNotification(notificationMessage);

            log.info("✅ Payment completed successfully: {}", paymentId);

        } else {
            // FAILED
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);

            log.error("❌ Payment failed: {}, ResponseCode: {}, TransactionStatus: {}", 
                    paymentId, responseCode, transactionStatus);

            // GỬI NOTIFICATION THẤT BẠI
            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .userId(payment.getUserId())
                    .title("Nạp tiền thất bại")
                    .message(getFailureMessage(responseCode, transactionStatus))
                    .type("DEPOSIT_FAILED")
                    .build();
            messagePublisher.publishNotification(notificationMessage);
        }

        return mapToPaymentResponse(payment);

    } catch (Exception e) {
        log.error("💥 Error processing VNPay callback", e);
        throw new PaymentException("Lỗi xử lý callback từ VNPay: " + e.getMessage());
    }
}

private String getFailureMessage(String responseCode, String transactionStatus) {
    //  Ưu tiên check responseCode (nếu có)
    if (responseCode != null && !responseCode.isEmpty()) {
        switch (responseCode) {
            case "07": return "Giao dịch bị nghi ngờ gian lận";
            case "09": return "Thẻ/Tài khoản chưa đăng ký dịch vụ InternetBanking";
            case "10": return "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11": return "Giao dịch đã hết hạn thanh toán. Vui lòng thử lại";
            case "12": return "Thẻ/Tài khoản bị khóa";
            case "13": return "Mã OTP không đúng. Vui lòng kiểm tra lại";
            case "24": return "Bạn đã hủy giao dịch";
            case "51": return "Tài khoản không đủ số dư để thực hiện giao dịch";
            case "65": return "Tài khoản đã vượt quá hạn mức giao dịch trong ngày";
            case "75": return "Ngân hàng thanh toán đang bảo trì. Vui lòng thử lại sau";
            case "79": return "Nhập sai mật khẩu thanh toán quá số lần quy định";
            default: return String.format("Giao dịch không thành công (Mã lỗi: %s)", responseCode);
        }
    }
    
    //  Fallback: check transactionStatus
    if (transactionStatus != null) {
        switch (transactionStatus) {
            case "00": return "Giao dịch thành công";
            case "01": return "Giao dịch chưa hoàn tất. Vui lòng hoàn tất thanh toán hoặc thử lại";
            case "02": return "Giao dịch bị hủy hoặc không thành công. Vui lòng thử lại";
            default: return String.format("Giao dịch không thành công (Trạng thái: %s)", transactionStatus);
        }
    }
    
    return "Giao dịch không thành công. Vui lòng liên hệ hỗ trợ nếu vấn đề tiếp diễn.";
}

private String buildPaymentUrl(Payment payment, VNPayPaymentRequest request, HttpServletRequest httpRequest) {
    try {
        String vnp_TmnCode = vnPayConfig.getTmnCode();
        String vnp_Amount = String.valueOf(request.getAmount().multiply(new BigDecimal(100)).longValue());
        String vnp_TxnRef = payment.getId();
        String vnp_OrderInfo = request.getDescription() != null ? request.getDescription()
                : "Nap tien vao tai khoan";
        String vnp_Locale = "vn";
        String vnp_ReturnUrl = vnPayConfig.getReturnUrl();
        String vnp_IpAddr = VNPayUtil.getIpAddress(httpRequest);

        log.info("=== VNPAY CONFIG ===");
        log.info("TmnCode: {}", vnp_TmnCode);
        log.info("Amount: {}", vnp_Amount);
        log.info("ReturnUrl: {}", vnp_ReturnUrl);
        log.info("HashSecret: {}", vnPayConfig.getHashSecret() != null ? "EXISTS" : "NULL");

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        if (request.getBankCode() != null && !request.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", request.getBankCode());
            log.info("🏦 Pre-selected bank: {}", request.getBankCode());
        } else {
            log.info("🏦 No bank selected, VNPay will show bank selection page");
        }

        log.info("=== VNPAY PARAMS ===");
        vnp_Params.forEach((key, value) -> log.info("{} = {}", key, value));

        String paymentUrl = VNPayUtil.getPaymentUrl(vnp_Params, vnPayConfig.getVnpayUrl(), vnPayConfig.getHashSecret());
        
        log.info("=== FINAL PAYMENT URL ===");
        log.info("{}", paymentUrl);
        
        return paymentUrl;
        
    } catch (Exception e) {
        log.error("Error building VNPay payment URL", e);
        throw new PaymentException("Không thể tạo URL thanh toán VNPay");
    }
}
    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .paymentType(payment.getPaymentType())
                .status(payment.getStatus())
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
