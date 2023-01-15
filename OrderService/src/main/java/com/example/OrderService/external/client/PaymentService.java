package com.example.OrderService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.OrderService.external.request.PaymentRequest;


@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
	@PostMapping
	public ResponseEntity<Long> doPayment( @RequestBody PaymentRequest paymentRequest );


}
 	