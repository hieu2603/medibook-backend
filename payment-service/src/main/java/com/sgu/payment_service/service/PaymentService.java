package com.sgu.payment_service.service;

import com.sgu.payment_service.dto.request.WithdrawRequest;
import com.sgu.payment_service.dto.response.payment.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse withdraw(WithdrawRequest request);
    List<PaymentResponse> getPaymentHistory(String userId);
    PaymentResponse getPaymentById(String paymentId);
}