package com.example.pizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.ProductType;
import com.example.pizza.repository.ComboRepository;
import com.example.pizza.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComboService {
    @Autowired
    private final ComboRepository comboRepository;

    @Autowired
    private ProductRepository productRepository;

    public Map<ProductType, Object> getComboOptionsWithQuantities(Combo combo) {
        Map<ProductType, Object> productOptions = new HashMap<>();

        if (combo.getPizzaCate() != null) {
            List<Product> pizzaProducts = productRepository.findByCategory(combo.getPizzaCate());
            productOptions.put(ProductType.PIZZA, Map.of(
                    "products", pizzaProducts,
                    "maxQuantity", combo.getPizzaQuantity()));
        }

        // Nếu drinkCate không null, thêm vào map
        if (combo.getDrinkCate() != null) {
            List<Product> drinkProducts = productRepository.findByCategory(combo.getDrinkCate());
            productOptions.put(ProductType.DRINK, Map.of(
                    "products", drinkProducts,
                    "maxQuantity", combo.getDrinkQuantity()));
        }

        // Nếu sideDishCate không null, thêm vào map
        if (combo.getSideDishCate() != null) {
            List<Product> sideDishProducts = productRepository.findByCategory(combo.getSideDishCate());
            productOptions.put(ProductType.SIDE_DISH, Map.of(
                    "products", sideDishProducts,
                    "maxQuantity", combo.getSideDishQuantity()));
        }

        return productOptions;
    }

    public ComboService(ComboRepository comboRepository) {
        this.comboRepository = comboRepository;
    }

    // Lấy danh sách tất cả combo
    public List<Combo> getAllCombos() {
        return comboRepository.findAll();
    }

    // Lấy thông tin combo theo ID
    public Combo getComboById(Integer id) {
        return comboRepository.findById(id).orElse(null);
    }

}
