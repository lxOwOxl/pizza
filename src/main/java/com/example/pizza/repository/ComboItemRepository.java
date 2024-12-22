package com.example.pizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizza.entity.ComboItem;

public interface ComboItemRepository extends JpaRepository<ComboItem, Long> {

}
