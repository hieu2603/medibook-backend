package com.sgu.payment_service.service;

import com.sgu.payment_service.dto.request.VNPayPaymentRequest;
import com.sgu.payment_service.dto.response.payment.VNPayPaymentResponse;
import com.sgu.payment_service.dto.response.payment.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VNPayService {
    VNPayPaymentResponse createPayment(VNPayPaymentRequest request, HttpServletRequest httpRequest);
    PaymentResponse handleCallback(Map<String, String> params);
}