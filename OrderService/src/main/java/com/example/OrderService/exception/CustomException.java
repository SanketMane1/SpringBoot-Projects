package com.example.OrderService.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
	
	private String errorCode;
	private int status;
	
	public CustomException(String msg,String errorCode, int i) {
		super(msg);
		this.errorCode = errorCode;
		this.status = i;
	}
	
	


}
