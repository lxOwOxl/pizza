package com.example.pizza.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Order;
import com.example.pizza.entity.OrderItem;
import com.example.pizza.enums.OrderStatus;
import com.example.pizza.enums.PaymentMethod;
import com.example.pizza.model.CartItem;
import com.example.pizza.model.UserDTO;
import com.example.pizza.repository.OrderItemRepository;
import com.example.pizza.repository.OrderRepository;
import com.paypal.api.payments.Payment;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PayPalService paypalService;

    @Autowired
    private CartService cartService;

    public Order createOrderCOD(BigDecimal totalAmount, PaymentMethod paymentMethod, UserDTO userDTO, String note) {

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (CartItem cartItem : cartService.getItems().values()) {

            OrderItem orderItem = new OrderItem();

        }
        Order order = new Order();
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        // Lưu đơn hàng vào cơ sở dữ liệu
        return orderRepository.save(order);
    }

    public Payment createOrderPayPal(BigDecimal totalAmount, String cancelUrl, String successUrl) throws Exception {
        // Tạo thanh toán PayPal
        return paypalService.createPayment(
                totalAmount,
                "VND",
                "paypal",
                "sale",
                "Thanh toán",
                cancelUrl,
                successUrl);
    }

    public Order completePayPalOrder(String paymentId, String payerId) throws Exception {
        // Xác nhận giao dịch PayPal
        Payment payment = paypalService.executePayment(paymentId, payerId);

        if (payment.getState().equals("approved")) {
            // Lưu thông tin đơn hàng sau khi thanh toán thành công
            Order order = new Order();
            order.setPaymentMethod(PaymentMethod.PAYPAL);
            order.setTotalAmount(new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal()));
            order.setStatus(OrderStatus.DELIVERED);
            order.setOrderDate(LocalDateTime.now());

            return orderRepository.save(order);
        }

        throw new Exception("Thanh toán không thành công.");
    }
}
