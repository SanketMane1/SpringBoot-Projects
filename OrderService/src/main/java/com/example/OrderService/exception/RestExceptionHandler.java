package com.example.OrderService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.OrderService.external.response.errorResponse;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<errorResponse> handleCustomException (CustomException exception){
		
		errorResponse err=new errorResponse().builder().errorMessage(exception.getMessage()).errorCode(exception.getErrorCode()).build();
		return new ResponseEntity<errorResponse>(err,HttpStatus.valueOf(exception.getStatus()));
		
	}
}
