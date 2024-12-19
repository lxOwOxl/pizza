package com.example.pizza.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pizza.entity.Combo;

public interface ComboRepository extends JpaRepository<Combo, Integer> {
    @Query("SELECT c.price FROM Combo c WHERE c.id = :id")
    BigDecimal findPriceById(@Param("id") Integer id);

}
