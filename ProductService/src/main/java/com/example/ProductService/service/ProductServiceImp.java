package com.example.ProductService.service;

import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProductService.entity.Product;
import com.example.ProductService.entity.ProductRequest;
import com.example.ProductService.entity.ProductResponse;
import com.example.ProductService.exception.ProductServiceException;
import com.example.ProductService.repository.ProductRepository;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImp implements ProductService {
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public long addProduct(ProductRequest productRequest) {
		// TODO Auto-generated method stub
		log.info("Adding Product...");
		
		Product product=Product.builder().productName(productRequest.getProductName())
										 .price(productRequest.getPrice())
										 .quantity(productRequest.getQuantity())
										 .build();
		
		 productRepository.save(product);
		
		
		log.info("Adding Product...");

		return product.getProductId();
	}

	@Override
	public ProductResponse getProduct(long productId) {
		// TODO Auto-generated method stub
		log.info("Get product for product id: {}",productId);
		Product product=productRepository.findById(productId).orElseThrow(()-> new ProductServiceException("Product with given id not available","Product_Not_Found"));
		ProductResponse productResponse=new ProductResponse();
		BeanUtils.copyProperties(product, productResponse);
		return productResponse;
	}

	

}
