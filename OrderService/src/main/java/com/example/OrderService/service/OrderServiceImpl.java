package com.example.OrderService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.OrderService.controller.OrderController;
import com.example.OrderService.entity.Order;
import com.example.OrderService.external.client.PaymentService;
import com.example.OrderService.external.client.ProductService;
import com.example.OrderService.external.request.PaymentRequest;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.repository.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	PaymentService paymentService;

	@Override
	public long placeOrder(OrderRequest orderRequest) {
		// TODO Auto-generated method stub
		
		log.info("Placing Order {}",orderRequest);
		
		productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
		
		log.info("Placing Order with reduced quantity{}",orderRequest);
		
		Order order = new Order().builder().amount(orderRequest.getTotalAmount())
											.orderStatus("CREATED")
											.productId(orderRequest.getProductId())
											.orderDate(Instant.now())
											.quantity(orderRequest.getQuantity())
											.build();
		order= orderRepository.save(order);
		log.info("Calling Payment service:");
		
		PaymentRequest paymentRequest=PaymentRequest.builder()
										.orderId(order.getId())
										.paymentMode(orderRequest.getPaymentMode())
										.amount(orderRequest.getTotalAmount())
										.build();
		
		String orderStatus=null;
		
		try {
			paymentService.doPayment(paymentRequest);
			log.info("Payment Done: Success");
			orderStatus="PLACED";

			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("Payment Done: Failed");
			orderStatus="PAYMENT-FAILED";
		}
		
		order.setOrderStatus(orderStatus);
		orderRepository.save(order);

		log.info("Order placed with order id: {}",order.getId());
		

		
		return order.getId();
	}
	
	

}
