package com.example.pizza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.ProductType;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByType(ProductType type);
}
