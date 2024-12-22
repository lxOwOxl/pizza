package com.example.pizza.controller;

import com.example.pizza.entity.User;
import com.example.pizza.enums.PaymentMethod;
import com.example.pizza.model.CheckoutForm;
import com.example.pizza.model.UserDTO;
import com.example.pizza.service.CartService;
import com.example.pizza.service.CouponService;
import com.example.pizza.service.CurrencyExchangeService;
import com.example.pizza.service.OrderService;
import com.example.pizza.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    private final PayPalService paypalService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @PostMapping("/create")
    public RedirectView createPayment(@ModelAttribute CheckoutForm checkoutForm,
            RedirectAttributes redirectAttributes, HttpSession session) {
        try {

            BigDecimal finalAmount = couponService.calculateFinalAmountByCoupon(checkoutForm.getCoupon(),
                    cartService.getTotalAmount()).add(cartService.getDefaultShippingFee());
            checkoutForm.setFinalAmount(finalAmount);

            session.setAttribute("checkoutForm", checkoutForm);

            if (checkoutForm.getPaymentMethod() == PaymentMethod.PAYPAL) {
                try {

                    // URL hủy và thành công
                    String cancelUrl = "http://localhost:8080/payment/cancel";
                    String successUrl = "http://localhost:8080/payment/success";

                    // Chỉ xử lý qua PayPal nếu chọn PayPal
                    BigDecimal exchangeAmount = currencyExchangeService.convertVNDToUSD(finalAmount);
                    System.out.println(exchangeAmount.toString());
                    Payment payment = paypalService.createPayment(
                            exchangeAmount,
                            "USD",
                            "paypal",
                            "sale",
                            "Thanh toán",
                            cancelUrl,
                            successUrl);
                    // Redirect đến PayPal để thanh toán
                    return new RedirectView(payment.getLinks().get(1).getHref());

                } catch (Exception e) {
                    System.err.println(e.getMessage());

                }
                return new RedirectView("/payment/error");
            } else if (checkoutForm.getPaymentMethod() == PaymentMethod.COD) {
                System.out.println("checkoutForm.getFinalAmount()" + checkoutForm.getFinalAmount());
                orderService.createOrderCOD(checkoutForm, cartService.getItems());

            }

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("checkoutForm", checkoutForm);
        }
        return new RedirectView("/cart/checkout");
    }

    @GetMapping("/success")
    public ModelAndView paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId, HttpSession session) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                CheckoutForm checkoutForm = (CheckoutForm) session.getAttribute("checkoutForm");
                orderService.createOrderCOD(checkoutForm, cartService.getItems());

                return new ModelAndView("customer/payment/payment-success");

            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new ModelAndView("customer/payment/payment-success");

    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "customer/payment/payment-cancel";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "customer/payment/payment-error";
    }

    @GetMapping("/success/view")
    public String showSuccessView() {
        return "customer/payment/payment-success";
    }
}
