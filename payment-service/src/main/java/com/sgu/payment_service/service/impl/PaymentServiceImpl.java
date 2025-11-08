package com.sgu.payment_service.service.impl;

import com.sgu.payment_service.dto.message.BalanceUpdateMessage;
import com.sgu.payment_service.dto.message.NotificationMessage;
import com.sgu.payment_service.dto.request.PaymentRequestDto;
import com.sgu.payment_service.dto.request.WithdrawRequest;
import com.sgu.payment_service.dto.response.payment.PaymentResponse;
import com.sgu.payment_service.enums.BalanceOperation;
import com.sgu.payment_service.enums.PaymentStatus;
import com.sgu.payment_service.client.UserServiceClient;
import com.sgu.payment_service.dto.response.payment.ApiResponse;
import com.sgu.payment_service.enums.PaymentType;
import com.sgu.payment_service.exception.PaymentException;
import com.sgu.payment_service.exception.PaymentNotFoundException;
import com.sgu.payment_service.model.Payment;
import com.sgu.payment_service.repository.PaymentRepository;
import com.sgu.payment_service.service.MessagePublisher;
import com.sgu.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final MessagePublisher messagePublisher;
    private final UserServiceClient userServiceClient;
    
    @Override
    @Transactional
    public PaymentResponse withdraw(WithdrawRequest request) {
    try {
        log.info("💰 Processing withdrawal for user: {}, amount: {}", request.getUserId(), request.getAmount());
        
        // Tạo payment record với PENDING
        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentType(PaymentType.WITHDRAW)
                .status(PaymentStatus.PENDING)
                .paymentMethod("BANK_TRANSFER")
                .bankAccount(request.getBankAccount())
                .bankName(request.getBankName())
                .description(request.getDescription() != null ? request.getDescription() : "Rút tiền về ngân hàng")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        payment = paymentRepository.save(payment);
        log.info("✅ Created withdrawal payment: {}", payment.getId());
        
        // 🔄 GỌI USER SERVICE ĐỒNG BỘ - TRỪ TIỀN TRƯỚC
        try {
            PaymentRequestDto paymentRequest = PaymentRequestDto.builder()
                    .userId(UUID.fromString(request.getUserId()))
                    .amount(request.getAmount())
                    .type(PaymentType.WITHDRAW)
                    .build();
            
            log.info("📞 Calling User Service to withdraw for user: {}", request.getUserId());
            
            ResponseEntity<ApiResponse<Void>> response = userServiceClient.withdraw(
                    UUID.fromString(request.getUserId()), 
                    paymentRequest
            );
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                // User Service báo lỗi (ví dụ: không đủ tiền)
                payment.setStatus(PaymentStatus.FAILED);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                
                log.error("❌ User Service failed: {}", response.getBody().getMessage());
                throw new PaymentException(response.getBody().getMessage());
            }
            
            log.info("✅ Balance decreased successfully");
            
        } catch (Exception e) {
            log.error("❌ Error calling User Service to decrease balance", e);
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            
            throw new PaymentException("Không thể trừ tiền: " + e.getMessage());
        }
        
        // ✅ Simulate withdrawal processing to bank
        boolean withdrawalSuccessful = processWithdrawal(payment);
        
        if (withdrawalSuccessful) {
            // Update status to COMPLETED
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setUpdatedAt(LocalDateTime.now());
            payment.setTransactionId("WD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            payment = paymentRepository.save(payment);
            
            // 📤 GỬI NOTIFICATION
            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .userId(request.getUserId())
                    .title("Rút tiền thành công")
                    .message(String.format("Bạn đã rút thành công %s VNĐ về tài khoản %s (%s)", 
                            String.format("%,d", request.getAmount().intValue()),
                            request.getBankName(),
                            maskBankAccount(request.getBankAccount())))
                    .type("WITHDRAW_SUCCESS")
                    .build();
            messagePublisher.publishNotification(notificationMessage);
            
            log.info("✅ Withdrawal completed successfully for payment: {}", payment.getId());
            
        } else {
            // ❌ Withdrawal failed - ROLLBACK: Gọi User Service cộng lại tiền
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);
            
            try {
                PaymentRequestDto rollbackRequest = PaymentRequestDto.builder()
                        .userId(UUID.fromString(request.getUserId()))
                        .amount(request.getAmount())
                        .type(PaymentType.DEPOSIT)  // Hoàn tiền = Deposit
                        .build();
                
                userServiceClient.deposit(UUID.fromString(request.getUserId()), rollbackRequest);
                log.info("✅ Balance rollback successful");
                
            } catch (Exception e) {
                log.error("❌ CRITICAL: Failed to rollback balance for user: {}", request.getUserId(), e);
                // TODO: Alert admin
            }
            
            // 📤 Gửi notification thất bại
            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .userId(request.getUserId())
                    .title("Rút tiền thất bại")
                    .message("Yêu cầu rút tiền không thành công. Số tiền đã được hoàn lại vào tài khoản.")
                    .type("WITHDRAW_FAILED")
                    .build();
            messagePublisher.publishNotification(notificationMessage);
            
            log.error("❌ Withdrawal failed for payment: {}", payment.getId());
            throw new PaymentException("Không thể xử lý yêu cầu rút tiền");
        }
        
        return mapToPaymentResponse(payment);
        
    } catch (Exception e) {
        log.error("❌ Error processing withdrawal request", e);
        throw new PaymentException("Không thể xử lý yêu cầu rút tiền: " + e.getMessage());
    }
}
    
    @Override
    public List<PaymentResponse> getPaymentHistory(String userId) {
        log.info("📜 Fetching payment history for user: {}", userId);
        List<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return payments.stream()
                .map(this::mapToPaymentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public PaymentResponse getPaymentById(String paymentId) {
        log.info("🔍 Fetching payment details for payment: {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Không tìm thấy giao dịch với ID: " + paymentId));
        return mapToPaymentResponse(payment);
    }
    

    private boolean processWithdrawal(Payment payment) {
        try {
            log.info("🏦 Processing withdrawal to bank: {} - Account: {}", 
                    payment.getBankName(), 
                    maskBankAccount(payment.getBankAccount()));
            

            // Simulate processing time
            Thread.sleep(1000);
            
            return true;
            
        } catch (InterruptedException e) {
            log.error("Error simulating withdrawal processing", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .paymentType(payment.getPaymentType())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .bankName(payment.getBankName())
                .bankAccount(payment.getBankAccount() != null ? maskBankAccount(payment.getBankAccount()) : null)
                .transactionId(payment.getTransactionId())
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
    
    /**
     * Mask bank account number for security
     * Example: 1234567890 -> ******7890
     */
    private String maskBankAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }
        int visibleDigits = 4;
        int totalLength = accountNumber.length();
        String masked = "*".repeat(totalLength - visibleDigits);
        return masked + accountNumber.substring(totalLength - visibleDigits);
    }
}


