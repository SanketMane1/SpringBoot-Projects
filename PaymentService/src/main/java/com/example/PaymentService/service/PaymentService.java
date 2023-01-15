package com.example.PaymentService.service;

import com.example.PaymentService.model.PaymentRequest;
import com.example.PaymentService.model.PaymentResponse;

public interface PaymentService {

	public Long doPayment(PaymentRequest paymentRequest) ;

	public PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
