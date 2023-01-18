package com.example.ProductService.service;

import org.springframework.stereotype.Service;

import com.example.ProductService.entity.ProductRequest;
import com.example.ProductService.entity.ProductResponse;

public interface ProductService {

	long addProduct(ProductRequest productRequest);

	ProductResponse getProduct(long productId);

	void reduceQuantity(long productId, long quantity);


}
