package com.example.pizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.example.pizza.model.Cart;
import com.example.pizza.model.CartItem;
import com.example.pizza.model.ProductDTO;
import com.example.pizza.service.CartService;
import com.example.pizza.service.ComboService;
import com.example.pizza.service.ProductService;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String showMenu(Model model) {
        List<Product> combos = productService.getProductsByType(ProductType.COMBO);
        List<Product> pizzas = productService.getProductsByType(ProductType.PIZZA);
        List<Product> drinks = productService.getProductsByType(ProductType.DRINK);
        List<Product> sideDishes = productService.getProductsByType(ProductType.SIDE_DISH);
        model.addAttribute("combos", combos);
        model.addAttribute("pizzas", pizzas);
        model.addAttribute("drinks", drinks);
        model.addAttribute("sideDishes", sideDishes);
        return "customer/menu/menu";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        List<ProductPrice> productPrices = productService.getPriceListByProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("quantity", 1);
        model.addAttribute("key", -1); // Xác định xem là thêm hay chỉnh sửa sản phẩm
        model.addAttribute("productPrices", productPrices);
        if (product.getType() == ProductType.PIZZA) {
            List<CrustPrice> mediumCrustPrices = productService.getCrustPriceListBySize(Size.MEDIUM);
            List<CrustPrice> largeCrustPrices = productService.getCrustPriceListBySize(Size.LARGE);
            model.addAttribute("selectCrust", Crust.TRADITIONAL);
            model.addAttribute("selectSize", Size.MEDIUM);
            model.addAttribute("mediumCrustPrices", mediumCrustPrices); // chứa giá theo size lớn
            model.addAttribute("largeCrustPrices", largeCrustPrices); // chứa giá theo size vừa
            // chứa giá theo từng size
        }

        return "customer/menu/customize-product";
    }

    @GetMapping("combo/{id}")
    public String getCombo(@PathVariable int productId) {

        return new String();
    }

}
