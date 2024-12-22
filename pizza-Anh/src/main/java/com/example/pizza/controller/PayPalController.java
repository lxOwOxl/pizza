package com.example.pizza.controller;

import com.example.pizza.entity.User;
import com.example.pizza.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam String paymentId,
            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            return "Thanh toán thành công: " + payment.getState();
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return "Lỗi khi thanh toán!";
        }
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "Thanh toán bị hủy!";
    }

    @PostMapping("/payment")
    public String startPayment(@RequestParam BigDecimal totalAmount, @ModelAttribute User user) {
        try {
            // Tạo yêu cầu thanh toán PayPal
            System.out.println("Total amount: " + totalAmount);
            BigDecimal bigDecimal = new BigDecimal(500);
            Payment payment = payPalService.createPayment(
                    bigDecimal, "USD", "paypal", "sale",
                    "Thanh toán cho đơn hàng",
                    "http://localhost:8080/payment/cancel",
                    "http://localhost:8080/payment/success");

            // Kiểm tra các liên kết trả về từ PayPal để chuyển hướng
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    // Trả về URL chuyển hướng đến PayPal để người dùng thực hiện thanh toán
                    return "Chuyển hướng đến PayPal: " + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            // Log lỗi chi tiết để xử lý khi gặp vấn đề
            e.printStackTrace();
            return "Lỗi khi tạo thanh toán! Vui lòng thử lại.";
        }
        return "Lỗi khi tạo thanh toán! Không tìm thấy liên kết chuyển hướng.";
    }

}
