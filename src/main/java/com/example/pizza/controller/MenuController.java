package com.example.pizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.example.pizza.service.ComboService;
import com.example.pizza.service.ProductService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ComboService comboService;

    @GetMapping
    public String showMenu(Model model) {
        List<Combo> combos = comboService.getAllCombos();
        List<Product> pizzas = productService.getProductsByType(ProductType.PIZZA);
        List<Product> drinks = productService.getProductsByType(ProductType.DRINK);
        List<Product> sideDishes = productService.getProductsByType(ProductType.APPETIZER);
        model.addAttribute("combos", combos);
        model.addAttribute("pizzas", pizzas);
        model.addAttribute("drinks", drinks);
        model.addAttribute("sideDishes", sideDishes);
        return "customer/menu/menu";
    }

    @PostMapping("/product/custom")
    public String getProduct(@RequestParam int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("crustPrices", productService.getAllCrustPrice());

        return "customer/menu/customize-product";

    }

    @PostMapping("/combo/custom")
    public String getComboOptions(@RequestParam Integer id, Model model) {
        Combo combo = comboService.getComboById(id);

        Map<ProductType, Object> productOptions = comboService.getComboOptionsWithQuantities(combo);
        model.addAttribute("combo", combo);
        // Đưa dữ liệu vào Model
        model.addAttribute("productOptions", productOptions);

        // Trả về tên View (HTML file)
        return "customer/menu/combo-options"; // Tên file HTML, ví dụ: combo-options.html
    }

}
