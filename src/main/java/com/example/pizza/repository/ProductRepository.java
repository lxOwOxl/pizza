package com.example.pizza.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pizza.entity.Product;
import com.example.pizza.enums.Category;
import com.example.pizza.enums.ProductType;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByType(ProductType type);

    List<Product> findByCategory(Category category);

    @Query("SELECT p FROM Product p JOIN FETCH p.prices WHERE p.id = :id")
    Optional<Product> findByIdWithPrices(@Param("id") Integer id);

}
