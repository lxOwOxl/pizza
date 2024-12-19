package com.example.pizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizza.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
