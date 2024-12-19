package com.example.pizza.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;
import com.example.pizza.entity.User;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.example.pizza.model.Cart;
import com.example.pizza.model.CartItem;
import com.example.pizza.service.CartService;
import com.example.pizza.service.ComboService;
import com.example.pizza.service.ProductService;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ComboService comboService;
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addPizza(
            @RequestParam int productId, @RequestParam(required = false) Size size,
            @RequestParam(required = false) Crust crust,
            @RequestParam int quantity,
            @RequestParam int key) {

        Product product = productService.getProductById(productId);

        CartItem cartItem = new CartItem(productId, product.getName(), product.getImage(), size, crust,
                product.getType(), quantity);
        if (key != -1) {
            cartService.updateItem(cartItem, key);
        } else
            cartService.addCartItem(cartItem);

        return "redirect:/cart";
    }

    @PostMapping("/combo={id}/select")
    public String handleSelectedProducts(@PathVariable Integer id,
            @RequestParam String[] selectedProducts,
            @RequestParam int quantity,
            Model model) {
        List<Integer> productIds = Arrays.stream(selectedProducts)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        cartService.processSelectedProducts(id, productIds, quantity);

        return "redirect:/cart"; // Tên file HTML hiển thị kết quả
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        model.addAttribute("cartItems", cartService.getItems());
        return "customer/cart/list";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam int key) {
        cartService.removeItem(key);
        return "redirect:/cart";

    }

    @GetMapping("/edit")
    public String editCartItem(
            @RequestParam Integer key,
            Model model) {
        System.out.println("Key item to edit" + key);
        CartItem itemToEdit = cartService.getItem(key);

        if (itemToEdit == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        List<ProductPrice> productPrices = productService.getPriceListByProduct(itemToEdit.getId());
        Product product = productService.getProductById(itemToEdit.getId());

        model.addAttribute("product", product);
        model.addAttribute("quantity", itemToEdit.getQuantity());
        model.addAttribute("key", key);
        model.addAttribute("productPrices", productPrices);

        if (itemToEdit.getType() == ProductType.PIZZA) {
            List<CrustPrice> mediumCrustPrices = productService.getCrustPriceListBySize(Size.MEDIUM);
            List<CrustPrice> largeCrustPrices = productService.getCrustPriceListBySize(Size.LARGE);
            model.addAttribute("selectCrust", itemToEdit.getCrust());
            model.addAttribute("selectSize", itemToEdit.getSize());
            model.addAttribute("mediumCrustPrices", mediumCrustPrices); // chứa giá theo size lớn
            model.addAttribute("largeCrustPrices", largeCrustPrices); // chứa giá theo size vừa
            // chứa giá theo từng size
        }
        return "customer/menu/customize-product";
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model) {
        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        return "customer/cart/payment";
    }

    @PostMapping("/checkout")
    public String submitCheckoutForm(@ModelAttribute User user,
            @RequestParam String discountCode,
            Model model) {
        // Xử lý thông tin người dùng và mã giảm giá
        double discount = 0;
        if ("DISCOUNT10".equals(discountCode)) {
            discount = 0.1; // Giảm 10% nếu mã là 'DISCOUNT10'
        }

        double totalAmount = 50000; // Tổng tiền gốc
        double shippingFee = 22000; // Phí giao hàng
        double discountAmount = totalAmount * discount;
        double finalAmount = totalAmount + shippingFee - discountAmount;

        // Đưa thông tin thanh toán lên model
        model.addAttribute("finalAmount", finalAmount);
        return "confirmation"; // Chuyển hướng đến trang xác nhận
    }
}
