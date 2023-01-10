package com.example.ProductService.exception;

import lombok.Data;

@Data
public class ProductServiceException extends RuntimeException{
	
	private String errorCode;
	
	public ProductServiceException(String string,String errorCode) {
		// TODO Auto-generated constructor stub
		super(string);
		this.errorCode=errorCode;
	}

	
	
	

}
