package com.example.OrderService.model;

import java.time.Instant;

import com.example.OrderService.external.response.PaymentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class OrderResponse {
	
	private long orderId;
	private Instant orderDate;
	private String orderStatus;
	private long amount;
	private ProductDetails productDetails;
	private PaymentDetails paymentDetails;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ProductDetails {
		
		private long productId;
		private String productName;
		private long price;
		private long quantity;


	}
	
	@Data
	@AllArgsConstructor
	@Builder
	public static class PaymentDetails {
		
		private long paymentId;
		private String status;
		private PaymentMode paymentMode;
		private Instant paymentDate; 
		
		

	}

}
