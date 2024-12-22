package com.example.pizza.repository;

import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;
import com.example.pizza.enums.Size;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer> {
    @Query("SELECT pp FROM ProductPrice pp WHERE pp.product.id = :productId")
    List<ProductPrice> findByProductId(@Param("productId") Integer productId);

    List<ProductPrice> findByProduct(Product product);

    @Query("SELECT p FROM ProductPrice p WHERE p.product.id = :productId AND " +
            "(p.size = :size OR (p.size IS NULL AND :size IS NULL))")
    ProductPrice findByProductAndSize(@Param("productId") int productId, @Param("size") Size size);

    @Query("SELECT p FROM ProductPrice p WHERE p.product.id = :productId")
    ProductPrice findProductPriceByProductId(@Param("productId") int productId);

    @Query("SELECT p.price FROM ProductPrice p " +
            "WHERE p.product.id = :productId " +
            "AND (:size IS NULL AND p.size IS NULL OR p.size = :size)")
    BigDecimal findPriceByProductIdAndSize(@Param("productId") int productId, @Param("size") Size size);

    @Query("SELECT p.price FROM ProductPrice p WHERE p.product.id = :productId")
    BigDecimal findPriceByProductId(@Param("productId") int productId);
}