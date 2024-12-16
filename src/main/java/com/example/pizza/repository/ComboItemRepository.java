package com.example.pizza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pizza.entity.ComboItem;
import com.example.pizza.entity.Product;

public interface ComboItemRepository extends JpaRepository<ComboItem, Integer> {

}
