package com.example.pizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pizza.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
