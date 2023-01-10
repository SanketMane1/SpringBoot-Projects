package com.example.ProductService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProductService.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
