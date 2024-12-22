package com.example.pizza.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.ComboItem;
import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Order;
import com.example.pizza.entity.OrderItem;
import com.example.pizza.entity.Product;
import com.example.pizza.entity.User;
import com.example.pizza.enums.OrderStatus;
import com.example.pizza.enums.PaymentMethod;
import com.example.pizza.model.CartItem;
import com.example.pizza.model.CheckoutForm;
import com.example.pizza.model.UserDTO;
import com.example.pizza.repository.ComboItemRepository;
import com.example.pizza.repository.ComboRepository;
import com.example.pizza.repository.CrustPriceRepository;
import com.example.pizza.repository.OrderItemRepository;
import com.example.pizza.repository.OrderRepository;
import com.example.pizza.repository.ProductPriceRepository;
import com.example.pizza.repository.ProductRepository;
import com.paypal.api.payments.Payment;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ComboRepository comboRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private CrustPriceRepository crustPriceRepository;
    @Autowired
    private PayPalService paypalService;
    @Autowired
    private UserService userService;

    public Order createOrderCOD(CheckoutForm checkoutForm, Map<String, CartItem> cart) {
        User user = new User();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            user = userService.getUserByUserName(username);
        } else {
            user = new User(checkoutForm.getUserDTO().getName(), checkoutForm.getUserDTO().getEmail(),
                    checkoutForm.getUserDTO().getPhone(), checkoutForm.getUserDTO().getAddress(), "GUEST");
        }

        Order order = new Order(user, checkoutForm.getPaymentMethod(), OrderStatus.PENDING, LocalDateTime.now(),
                checkoutForm.getNote(), checkoutForm.getFinalAmount());
        for (CartItem cartItem : cart.values()) {
            if (cartItem.getProductId() != null) {
                Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
                CrustPrice crustPrice = crustPriceRepository.findById(cartItem.getCrustId()).orElse(null);
                BigDecimal price = productPriceRepository
                        .findPriceByProductIdAndSize(cartItem.getProductId(), crustPrice.getSize())
                        .add(crustPrice.getAdditionalPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                OrderItem orderItem = new OrderItem(order, product, null, cartItem.getQuantity(), crustPrice, price,
                        null);
                order.getOrderItems().add(orderItem);

            } else {
                Combo combo = comboRepository.findById(cartItem.getComboId()).orElse(null);
                List<ComboItem> comboItems = new ArrayList<>();
                for (Integer id : cartItem.getProductIds()) {
                    Product product = productRepository.findById(id).orElse(null);
                    CrustPrice crustPrice = crustPriceRepository.findByCrustAndSize(combo.getCrust(), combo.getSize());
                    ComboItem comboItem = new ComboItem(product, crustPrice);
                    comboItems.add(comboItem);
                }
                OrderItem orderItem = new OrderItem(order, null, combo, cartItem.getQuantity(), null, combo.getPrice(),
                        comboItems);
                order.getOrderItems().add(orderItem);

            }

        }

        return orderRepository.save(order);
    }

}
