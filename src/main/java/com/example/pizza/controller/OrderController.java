package com.example.pizza.controller;

import com.example.pizza.entity.User;
import com.example.pizza.enums.PaymentMethod;
import com.example.pizza.model.UserDTO;
import com.example.pizza.service.OrderService;
import com.example.pizza.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final PayPalService paypalService;

    @PostMapping("/create")
    public RedirectView createPayment(@RequestParam PaymentMethod paymentMethod,
            @RequestParam BigDecimal finalAmount, @RequestParam String note, @ModelAttribute UserDTO userDTO) {
        if (paymentMethod == PaymentMethod.COD) {
            orderService.createOrderCOD(finalAmount, paymentMethod, userDTO, note);
        } else {
            try {

                // URL hủy và thành công
                String cancelUrl = "http://localhost:8080/payment/cancel";
                String successUrl = "http://localhost:8080/payment/success";

                // Chỉ xử lý qua PayPal nếu chọn PayPal

                Payment payment = paypalService.createPayment(
                        finalAmount,
                        "VND", // Đơn vị tiền tệ
                        "paypal", // Phương thức thanh toán cố định là "paypal"
                        "sale", // Ý định thanh toán (sale/authorize/order)
                        "Thanh toán",
                        cancelUrl,
                        successUrl);
                // Redirect đến PayPal để thanh toán
                return new RedirectView(payment.getLinks().get(1).getHref());

            } catch (Exception e) {
                System.err.println(e.getMessage());

            }
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return "success";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }
}
