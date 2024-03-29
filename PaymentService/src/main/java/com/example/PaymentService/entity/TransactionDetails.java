package com.example.PaymentService.entity;



import java.time.Instant;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long orderId;
	private String paymentMode;
	private String referanceNumber;
	private Instant paymentdate;
	private String paymentStatus;
	private long amount;
	
	
	
	

}
