package com.example.pizza.enums;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    PIZZA_BASIC,
    PIZZA_PREMIUM,
    APPETIZER_SALAD,
    DRINK_CAN,
    DRINK_BOTTLE,
    DRINK_BEVERAGE,
    APPETIZER_PASTA;

    // Hàm lấy danh sách các Category theo type
    public static List<Category> getCategoriesByType(String type) {
        List<Category> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            // Kiểm tra nếu category name chứa type (ví dụ "PIZZA")
            if (category.name().contains(type)) {
                categories.add(category);
            }
        }
        return categories;
    }
}
