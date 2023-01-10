package com.example.ProductService.service;

import com.example.ProductService.entity.ProductRequest;
import com.example.ProductService.entity.ProductResponse;

public interface ProductService {

	long addProduct(ProductRequest productRequest);

	ProductResponse getProduct(long productId);


}
