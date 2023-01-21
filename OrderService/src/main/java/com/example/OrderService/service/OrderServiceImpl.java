package com.example.OrderService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.OrderService.entity.Order;
import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.client.PaymentService;
import com.example.OrderService.external.client.ProductService;
import com.example.OrderService.external.request.PaymentRequest;
import com.example.OrderService.external.response.PaymentResponse;
import com.example.OrderService.external.response.ProductResponse;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.model.OrderResponse;
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
	
	@Autowired
	RestTemplate restTemplate;

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

	@Override
	public OrderResponse getOrderDetails(Long orderId) {
		// TODO Auto-generated method stub
		log.info("get order details for order id: {}",orderId);
		Order order = orderRepository.findById(orderId)
										.orElseThrow(()->new CustomException("Order not found for order id :"+orderId, "NOT_FOUND", 404));
		
		log.info("Invoking Product Service to fetch product with id : {}",order.getId());
		ProductResponse productResponse=restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(), ProductResponse.class);
		
		OrderResponse.ProductDetails productDetails=OrderResponse.ProductDetails.builder()
													.productId(productResponse.getProductId())
													.productName(productResponse.getProductName())
													.price(productResponse.getPrice())
													.quantity(productResponse.getQuantity())
													.build();
		
		log.info("Invoking Payment Service to fetch Payment with id : {}",order.getId());
		PaymentResponse paymentResponse=restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class);
		
		OrderResponse.PaymentDetails paymentDetails=OrderResponse.PaymentDetails.builder()
				.paymentId(paymentResponse.getPaymentId())
				.paymentMode(paymentResponse.getPaymentMode())
				.paymentDate(paymentResponse.getPaymentDate())
				.status(paymentResponse.getStatus())
				.build();

		OrderResponse orderResponse=OrderResponse.builder()
									.orderId(order.getId())
									.orderStatus(order.getOrderStatus())
									.orderDate(order.getOrderDate())
									.amount(order.getAmount())
									.productDetails(productDetails)
									.paymentDetails(paymentDetails)
									.build();
		return orderResponse;
	}
	
	

}
