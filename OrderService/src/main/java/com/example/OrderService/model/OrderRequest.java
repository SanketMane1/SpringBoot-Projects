package com.example.OrderService.model;

import java.time.Instant;

import com.example.OrderService.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
	
	private long productId;
	private long quantity;
	private long totalAmount;
	private PaymentMode paymentMode;

}
