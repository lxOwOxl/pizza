package com.example.pizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pizza.entity.Combo;

public interface ComboRepository extends JpaRepository<Combo, Integer> {
}
