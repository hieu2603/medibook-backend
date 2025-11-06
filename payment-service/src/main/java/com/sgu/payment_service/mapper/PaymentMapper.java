package com.sgu.payment_service.mapper;

import com.sgu.payment_service.dto.response.payment.PaymentResponse;
import com.sgu.payment_service.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    
    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
        
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
