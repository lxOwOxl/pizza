package com.example.pizza.service;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.ProductType;
import com.example.pizza.model.ComboDTO;
import com.example.pizza.model.ProductDTO;
import com.example.pizza.repository.ComboRepository;
import com.example.pizza.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComboService {
    @Autowired
    private final ComboRepository comboRepository;

    @Autowired
    private ProductRepository productRepository;

    public ComboDTO getComboDTO(int comboId, List<Integer> productIds) {
        Combo combo = comboRepository.findById(comboId)
                .orElseThrow(() -> new IllegalArgumentException("Combo not found"));
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Integer productId : productIds) {
            Product product = productRepository.findById(productId).orElse(null);
            ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getType(),
                    combo.getSize(), combo.getCrust());
            productDTOs.add(productDTO);
        }
        return new ComboDTO(combo.getId(), combo.getName(), combo.getImage(), combo.getPrice(), productDTOs);
    }

    public Map<ProductType, Object> getComboOptionsWithQuantities(Combo combo) {
        Map<ProductType, Object> productOptions = new HashMap<>();

        for (int i = 0; i < combo.getPizzaQuantity(); i++) {
            List<Product> pizzaProducts = productRepository.findByCategory(combo.getPizzaCate());
            productOptions.put(ProductType.PIZZA, Map.of(
                    "products", pizzaProducts,
                    "maxQuantity", combo.getPizzaQuantity()));
        }

        for (int i = 0; i < combo.getDrinkQuantity(); i++) {
            List<Product> drinkProducts = productRepository.findByCategory(combo.getDrinkCate());
            productOptions.put(ProductType.DRINK, Map.of(
                    "products", drinkProducts,
                    "maxQuantity", combo.getDrinkQuantity()));
        }

        for (int i = 0; i < combo.getSideDishQuantity(); i++) {
            List<Product> sideDishProducts = productRepository.findByCategory(combo.getSideDishCate());
            productOptions.put(ProductType.APPETIZER, Map.of(
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

    public BigDecimal getPriceById(int id) {
        return comboRepository.findPriceById(id);
    }
}
