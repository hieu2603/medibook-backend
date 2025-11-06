package com.sgu.payment_service.dto.response.payment;

import com.sgu.payment_service.enums.PaymentStatus;
import com.sgu.payment_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    // ===== BASIC INFO =====
    private String paymentId;           // ID giao dịch
    private String userId;              // User thực hiện
    private BigDecimal amount;          // Số tiền
    
    // ===== PAYMENT INFO =====
    private PaymentType paymentType;    // DEPOSIT / WITHDRAW
    private PaymentStatus status;       // PENDING / COMPLETED / FAILED
    private String paymentMethod;       // ← THÊM: VNPAY / BANK_TRANSFER / MOMO
    
    // ===== BANK INFO (cho WITHDRAW) =====
    private String bankName;            // Tên ngân hàng
    private String bankAccount;         // Số tài khoản (đã mask)
    
    // ===== TRANSACTION INFO =====
    private String transactionId;       // Mã giao dịch từ gateway/bank
    private String description;         // Mô tả
    
    // ===== TIMESTAMP =====
    private LocalDateTime createdAt;    // Thời gian tạo
    private LocalDateTime updatedAt;    // Thời gian cập nhật
}