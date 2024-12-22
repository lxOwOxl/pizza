package com.example.pizza.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Coupon;
import com.example.pizza.enums.DiscountType;
import com.example.pizza.repository.CouponRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public BigDecimal calculateFinalAmountByCoupon(String couponCode, BigDecimal orderAmount) {
        // Tìm coupon theo code
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("Mã coupon không hợp lệ"));

        // Kiểm tra trạng thái của coupon
        if (!coupon.getStatus()) {
            throw new IllegalArgumentException("Mã coupon đã bị vô hiệu hóa");
        }

        // Kiểm tra xem coupon có hết hạn không
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getEndDate().isBefore(now)) {
            throw new IllegalArgumentException("Mã coupon đã hết hạn");
        }

        // Kiểm tra xem coupon có được sử dụng với đơn hàng này không (ví dụ: có đủ điều
        // kiện giá trị đơn hàng không)
        if (coupon.getMinimumOrderAmount() != null && orderAmount.compareTo(coupon.getMinimumOrderAmount()) < 0) {
            throw new IllegalArgumentException("Đơn hàng không đủ điều kiện áp dụng mã coupon");
        }

        // Kiểm tra giới hạn sử dụng
        if (coupon.getUsageLimit() != null && coupon.getUsageLimit() == coupon.getCurrentUsage()) {
            throw new IllegalArgumentException("Mã coupon đã hết lượt sử dụng");
        }
        coupon.setCurrentUsage(coupon.getCurrentUsage() + 1);
        couponRepository.save(coupon);

        if (coupon.getDiscountType() == DiscountType.FIXED) {
            return orderAmount.subtract(coupon.getDiscountValue());
        } else if (coupon.getDiscountType() == DiscountType.PERCENT) {
            return orderAmount.multiply(BigDecimal.valueOf(1).subtract(coupon.getDiscountValue()));
        }
        return orderAmount;

    }
}
