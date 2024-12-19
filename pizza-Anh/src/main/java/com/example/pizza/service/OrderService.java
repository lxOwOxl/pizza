package com.example.pizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Order;
import com.example.pizza.entity.OrderItem;
import com.example.pizza.repository.OrderItemRepository;
import com.example.pizza.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    // public void addToCart(PizzaVariant pizzaVariant) {
    // // Lấy thông tin giỏ hàng của người dùng (giả sử đã có sẵn)
    // Order order = getCurrentOrder();

    // // Tạo OrderItem mới
    // OrderItem orderItem = new OrderItem();
    // orderItem.setPizzaVariant(pizzaVariant);
    // orderItem.setQuantity(1); // Giả sử số lượng là 1
    // orderItem.setPrice(pizzaVariant.getPrice());
    // orderItem.setOrder(order);

    // // Lưu OrderItem vào giỏ hàng
    // orderItemRepository.save(orderItem);
    // }

    // public Order getCurrentOrder() {
    // // // Lấy giỏ hàng của người dùng (có thể là giỏ hàng hiện tại hoặc tạo mới
    // nếu
    // // // chưa có)
    // // return orderRepository.findByUserAndStatus("USER", "IN_CART");
    // }
}
