package com.sgu.payment_service.controller;

import com.sgu.payment_service.dto.request.WithdrawRequest;
import com.sgu.payment_service.enums.PaymentStatus;
import com.sgu.payment_service.service.PaymentService;
import com.sgu.payment_service.dto.request.VNPayPaymentRequest;
import com.sgu.payment_service.dto.response.payment.VNPayPaymentResponse;
import com.sgu.payment_service.service.VNPayService;
import com.sgu.payment_service.dto.response.payment.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;  // ← THÊM
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;  // ← THÊM
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    
    private final PaymentService paymentService;
    private final VNPayService vnPayService;
    
    /**
     * POST /api/payments/deposit
     * Nạp tiền vào tài khoản (tăng balance trong User Service)
     */
    @PostMapping("/deposit")
    public ResponseEntity<VNPayPaymentResponse> createDeposit(
            @Valid @RequestBody VNPayPaymentRequest request,
            HttpServletRequest httpRequest) {
        log.info("Received deposit request for user: {}", request.getUserId());
        VNPayPaymentResponse response = vnPayService.createPayment(request, httpRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/payments/withdraw
     * Rút tiền (giảm balance)
     */
    @PostMapping("/withdraw")
    public ResponseEntity<PaymentResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        log.info("Received withdraw request for user: {}", request.getUserId());
        PaymentResponse response = paymentService.withdraw(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/payments/vnpay-callback
     * Callback từ VNPay sau khi user thanh toán
     * 
     */
    @GetMapping("/vnpay-callback")
    public void vnpayCallback(
            @RequestParam Map<String, String> params,
            HttpServletResponse response) throws IOException {
        
        log.info("Received VNPay callback");
        
        try {
            PaymentResponse paymentResponse = vnPayService.handleCallback(params);
            
            //  Trả về HTML
            response.setContentType("text/html; charset=UTF-8");
            String html = buildResultPage(paymentResponse);
            response.getWriter().write(html);
            
        } catch (Exception e) {
            log.error("Error processing VNPay callback", e);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(buildErrorPage(e.getMessage()));
        }
    }
    
    /**
     * POST /api/payments/vnpay-ipn
     * IPN (Instant Payment Notification) từ VNPay
     */
    @PostMapping("/vnpay-ipn")
    public ResponseEntity<Map<String, String>> vnpayIPN(@RequestParam Map<String, String> params) {
        log.info("Received VNPay IPN");
        Map<String, String> response = new HashMap<>();
        try {
            vnPayService.handleCallback(params);
            response.put("RspCode", "00");
            response.put("Message", "success");
        } catch (Exception e) {
            log.error("Error processing VNPay IPN", e);
            response.put("RspCode", "99");
            response.put("Message", "fail");
        }
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/payments/history/{userId}
     * Lấy lịch sử giao dịch theo user
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(@PathVariable String userId) {
        log.info("Fetching payment history for user: {}", userId);
        List<PaymentResponse> history = paymentService.getPaymentHistory(userId);
        return ResponseEntity.ok(history);
    }
    
    /**
     * GET /api/payments/{paymentId}
     * Lấy chi tiết một giao dịch
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        log.info("Fetching payment details for: {}", paymentId);
        PaymentResponse payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }
    
    //  THÊM: Method build HTML page
    private String buildResultPage(PaymentResponse payment) {
        boolean isSuccess = payment.getStatus() == PaymentStatus.COMPLETED;
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        background: linear-gradient(135deg, %s 0%%, %s 100%%);
                    }
                    .container {
                        background: white;
                        padding: 50px;
                        border-radius: 20px;
                        box-shadow: 0 20px 60px rgba(0,0,0,0.2);
                        text-align: center;
                        max-width: 500px;
                        width: 90%%;
                    }
                    .icon {
                        font-size: 80px;
                        margin-bottom: 20px;
                        animation: bounceIn 0.6s;
                    }
                    @keyframes bounceIn {
                        0%% { transform: scale(0); }
                        50%% { transform: scale(1.1); }
                        100%% { transform: scale(1); }
                    }
                    .title {
                        font-size: 28px;
                        font-weight: bold;
                        margin-bottom: 15px;
                        color: %s;
                    }
                    .info {
                        margin: 15px 0;
                        font-size: 16px;
                        color: #666;
                    }
                    .amount {
                        font-size: 40px;
                        font-weight: bold;
                        color: #333;
                        margin: 25px 0;
                    }
                    .payment-id {
                        background: #f5f5f5;
                        padding: 10px;
                        border-radius: 8px;
                        font-family: monospace;
                        font-size: 14px;
                        color: #666;
                        margin: 20px 0;
                        word-break: break-all;
                    }
                    .message {
                        margin: 20px 0;
                        font-size: 16px;
                        color: %s;
                        font-weight: 500;
                    }
                    button {
                        background: %s;
                        color: white;
                        border: none;
                        padding: 15px 40px;
                        border-radius: 10px;
                        font-size: 16px;
                        font-weight: 600;
                        cursor: pointer;
                        margin-top: 25px;
                        transition: all 0.3s;
                    }
                    button:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 5px 15px rgba(0,0,0,0.2);
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="icon">%s</div>
                    <div class="title">%s</div>
                    <div class="amount">%s VNĐ</div>
                    <div class="info">Mã giao dịch</div>
                    <div class="payment-id">%s</div>
                    <div class="message">%s</div>
                    <button onclick="window.close()">Đóng cửa sổ</button>
                </div>
            </body>
            </html>
            """,
            // title
            isSuccess ? "Thanh toán thành công" : "Thanh toán thất bại",
            // gradient colors
            isSuccess ? "#667eea" : "#f093fb",
            isSuccess ? "#764ba2" : "#f5576c",
            // title color
            isSuccess ? "#28a745" : "#dc3545",
            // message color
            isSuccess ? "#28a745" : "#dc3545",
            // button color
            isSuccess ? "#28a745" : "#dc3545",
            // icon
            isSuccess ? "✅" : "❌",
            // title text
            isSuccess ? "Thanh toán thành công!" : "Thanh toán thất bại",
            // amount
            String.format("%,d", payment.getAmount().intValue()),
            // payment id
            payment.getPaymentId(),
            // message
            isSuccess 
                ? "✨ Tiền đã được cộng vào tài khoản của bạn" 
                : "⚠️ Giao dịch không thành công. Vui lòng thử lại hoặc liên hệ hỗ trợ"
        );
    }
    
    //  THÊM: Method build error page
    private String buildErrorPage(String errorMessage) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Lỗi xử lý thanh toán</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        background: linear-gradient(135deg, #f5576c 0%, #ff6b6b 100%);
                    }
                    .container {
                        background: white;
                        padding: 40px;
                        border-radius: 15px;
                        text-align: center;
                        max-width: 400px;
                    }
                    .icon { font-size: 60px; margin-bottom: 20px; }
                    h2 { color: #dc3545; margin-bottom: 20px; }
                    p { color: #666; margin-bottom: 30px; }
                    button {
                        background: #dc3545;
                        color: white;
                        border: none;
                        padding: 12px 30px;
                        border-radius: 8px;
                        font-size: 16px;
                        cursor: pointer;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="icon">⚠️</div>
                    <h2>Lỗi xử lý thanh toán</h2>
                    <p>""" + errorMessage + """
                    </p>
                    <button onclick="window.close()">Đóng</button>
                </div>
            </body>
            </html>
            """;
    }
}