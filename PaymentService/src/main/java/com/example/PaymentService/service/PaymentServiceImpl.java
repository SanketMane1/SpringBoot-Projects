package com.example.PaymentService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PaymentService.entity.TransactionDetails;
import com.example.PaymentService.model.PaymentMode;
import com.example.PaymentService.model.PaymentRequest;
import com.example.PaymentService.model.PaymentResponse;
import com.example.PaymentService.repository.TransactionDetailsRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	TransactionDetailsRepository transactionDetailsRepository;

	@Override
	public Long doPayment(PaymentRequest paymentRequest) {
		// TODO Auto-generated method stub
		log.info("Recording Payment Details:");
		TransactionDetails transactionDetails=TransactionDetails.builder()
												.paymentdate(Instant.now())
												.paymentMode(paymentRequest.getPaymentMode().name())
												.paymentStatus("SUCCESS")
												.orderId(paymentRequest.getOrderId())
												.referanceNumber(paymentRequest.getReferanceNumber())
												.amount(paymentRequest.getAmount())
												.build();
		
		transactionDetailsRepository.save(transactionDetails);
		
		log.info("Completed with id {}:",transactionDetails.getId());
												
		return transactionDetails.getId();
	}

	@Override
	public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
		// TODO Auto-generated method stub
		log.info("Getting payment details for Order Id : {}",orderId);
		TransactionDetails transactionDetails =   transactionDetailsRepository.findByOrderId(orderId);
		PaymentResponse paymentResponse=PaymentResponse.builder()
										.paymentId(transactionDetails.getId())
										.paymentDate(transactionDetails.getPaymentdate())
										.paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
										.orderId(transactionDetails.getOrderId())
										.status(transactionDetails.getPaymentStatus())
										.amount(transactionDetails.getAmount())
										.build();
		return paymentResponse;
	}

}
