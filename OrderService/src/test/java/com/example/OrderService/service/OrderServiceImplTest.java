package com.example.OrderService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.OrderService.model.PaymentMode;
import com.example.OrderService.repository.OrderRepository;
import com.okta.commons.lang.Assert;

@SpringBootTest
public class OrderServiceImplTest {
	
	@Mock
	OrderRepository orderRepository;
	
	@Mock
	ProductService productService;
	
	@Mock
	PaymentService paymentService;
	
	@Mock
	RestTemplate restTemplate;
	
	
	@InjectMocks
	OrderService orderService=new OrderServiceImpl();
	
	@DisplayName("Get Order Success scenario")
	@Test
	void test_When_Order_Success() {
		//mocking
		Order order=getMockOrder();
		Mockito.when(orderRepository.findById(ArgumentMatchers.anyLong()))
				.thenReturn(Optional.of(order));
		
		Mockito.when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(), ProductResponse.class))
		.thenReturn(getMockProductResponse());

		
		Mockito.when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class))
		.thenReturn(getMockPaymentResponse());
		
		//actual
		OrderResponse orderResponse = orderService.getOrderDetails(1L);
		
	
		//verification
		Mockito.verify(orderRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
		
		Mockito.verify(restTemplate, Mockito.times(1)).getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(), ProductResponse.class);
		
		Mockito.verify(restTemplate, Mockito.times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class);


		
		//assert
		Assertions.assertNotNull(orderResponse);
		assertEquals(order.getId(), orderResponse.getOrderId());
		
		
	}
	
	@DisplayName("Get Order failure scenario")
	@Test
	void test_when_order_not_found() {
		
		Mockito.when(orderRepository.findById(ArgumentMatchers.anyLong()))
		.thenReturn(Optional.ofNullable(null));
		
		CustomException customException = assertThrows(CustomException.class, ()-> orderService.getOrderDetails(1L));
		
		assertEquals("NOT_FOUND", customException.getErrorCode());
		assertEquals(404, customException.getStatus());
		
		
		Mockito.verify(orderRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());

		
	}
	
	@DisplayName("Place order success scenario")
	@Test
	void test_when_placeOrder_success() {
		OrderRequest orderRequest=getMockOrderRequest();
		Order order=getMockOrder();
		
		
		Mockito.when(productService.reduceQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order);
		Mockito.when(paymentService.doPayment(any(PaymentRequest.class))).thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));

		
		long orderId=orderService.placeOrder(orderRequest);
		
		
		Mockito.verify(orderRepository, Mockito.times(2)).save(any());

		Mockito.verify(productService, Mockito.times(1)).reduceQuantity(anyLong(), anyLong());
		
		Mockito.verify(paymentService, Mockito.times(1)).doPayment(any(PaymentRequest.class));
		
		
		assertEquals(order.getId(), orderId);



		
		
		

		
	}
	
	@DisplayName("Place order success but payment failed scenario")
	@Test
	void test_when_placeOrder_success_paymentFailed() {
		OrderRequest orderRequest=getMockOrderRequest();
		Order order=getMockOrder();
		
		
		Mockito.when(productService.reduceQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
		Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order);
		Mockito.when(paymentService.doPayment(any(PaymentRequest.class))).thenThrow(new RuntimeException());

		
		long orderId=orderService.placeOrder(orderRequest);
		
		
		Mockito.verify(orderRepository, Mockito.times(2)).save(any());

		Mockito.verify(productService, Mockito.times(1)).reduceQuantity(anyLong(), anyLong());
		
		Mockito.verify(paymentService, Mockito.times(1)).doPayment(any(PaymentRequest.class));
		
		
		assertEquals(order.getId(), orderId);



		
		
		

		
	}

	private OrderRequest getMockOrderRequest() {
		// TODO Auto-generated method stub
		return OrderRequest.builder()
					.productId(2)
					.quantity(10)
					.paymentMode(PaymentMode.CASH)
					.totalAmount(100)
					.build();
	}

	private PaymentResponse getMockPaymentResponse() {
		// TODO Auto-generated method stub
		return PaymentResponse.builder()
				.paymentId(1)
				.paymentDate(Instant.now())
				.paymentMode(PaymentMode.CASH)
				.amount(200000)
				.orderId(1)
				.status("ACCEPTED")
				.build();
	}

	private ProductResponse getMockProductResponse() {
		// TODO Auto-generated method stub
		return ProductResponse.builder()
								.productId(2)
								.productName("iPhone")
								.price(100)
								.quantity(200)
								.build();
	}

	private Order getMockOrder() {
		// TODO Auto-generated method stub
		return Order.builder()
					.orderStatus("PLACED")
					.orderDate(Instant.now())
					.id(1)
					.amount(100)
					.quantity(200)
					.productId(2)
					.build();
	}

	

}
