package com.example.pizza.controller;

import com.example.pizza.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PayPalController {

    private final PayPalService paypalService;

   
    @PostMapping("/payment/create")
    public RedirectView createPayment(
        @RequestParam("paymentMethod") String paymentMethod, // Tên tham số từ form
        @RequestParam("totalAmount") String totalAmount,
        @RequestParam("description") String description,
        @RequestParam(value = "discountCode", required = false) String discountCode,
        @RequestParam("fullName") String fullName,
        @RequestParam("email") String email,
        @RequestParam("address") String address,
        @RequestParam("phoneNumber") String phoneNumber
        ) {
        try {
        // Tính tổng tiền cuối cùng nếu có mã giảm giá
        double finalAmount = Double.parseDouble(totalAmount);
        if ("DISCOUNT10".equalsIgnoreCase(discountCode)) {
            finalAmount -= finalAmount * 0.1; // Giảm 10%
        }

        // URL hủy và thành công
        String cancelUrl = "http://localhost:8080/payment/cancel";
        String successUrl = "http://localhost:8080/payment/success";

        // Chỉ xử lý qua PayPal nếu chọn PayPal
        if ("PayPal".equalsIgnoreCase(paymentMethod)) {
            Payment payment = paypalService.createPayment(
                    finalAmount,
                    "USD", // Đơn vị tiền tệ
                    "paypal", // Phương thức thanh toán cố định là "paypal"
                    "sale", // Ý định thanh toán (sale/authorize/order)
                    description,
                    cancelUrl,
                    successUrl
            );
            // Redirect đến PayPal để thanh toán
            return new RedirectView(payment.getLinks().get(1).getHref());
        }

        // Nếu không phải PayPal (ví dụ: COD), điều hướng về trang xác nhận
        return new RedirectView("/payment/confirm?status=success");// đang xem xét

    } catch (Exception e) {
        e.printStackTrace();
        return new RedirectView("/payment/error");
    }
}


    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return "paymentSuccess";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }
}
