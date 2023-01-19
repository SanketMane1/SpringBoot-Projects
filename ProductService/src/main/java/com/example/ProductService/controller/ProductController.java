package com.example.ProductService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProductService.entity.ProductRequest;
import com.example.ProductService.entity.ProductResponse;
import com.example.ProductService.repository.ProductRepository;
import com.example.ProductService.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
    @PreAuthorize("hasAuthority('Admin')")
	@PostMapping
	public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
		
		
		long productId = productService.addProduct(productRequest);
		
		return new ResponseEntity<Long>(productId,HttpStatus.CREATED);
		
	}
    
	
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") long productId){
		
		
		ProductResponse productResponse = productService.getProduct(productId);
		
		return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);	
	}
	
    
	@PutMapping("/reduceQuantity/{id}")
	public ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId,@RequestParam long quantity){
		productService.reduceQuantity(productId,quantity);
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	

}
