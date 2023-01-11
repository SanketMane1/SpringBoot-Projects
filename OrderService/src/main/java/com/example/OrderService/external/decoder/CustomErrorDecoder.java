package com.example.OrderService.external.decoder;

import java.io.IOException;

import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.response.errorResponse;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		// TODO Auto-generated method stub
		ObjectMapper objectMapper=new ObjectMapper();
		log.info("::{}",response.request().url());
		log.info("::{}",response.request().headers());
		
		try {
			errorResponse er=objectMapper.readValue(response.body().asInputStream(), errorResponse.class);
			return new CustomException(er.getErrorMessage(), er.getErrorCode(), response.status()) ;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CustomException("Internal IO Server Error", "INTERNAL_SERVER_ERROR", 500);
		}

		
	}

}
