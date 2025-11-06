package com.sgu.payment_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VNPayPaymentRequest {
    
    @NotBlank(message = "User ID không được để trống")
    private String userId;
    
    @NotNull(message = "Số tiền không được để trống")
    @Min(value = 10000, message = "Số tiền nạp tối thiểu là 10,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    // Optional - Không bắt buộc
    private String bankCode;
    
    private String language;
}