package com.example.pizza.controller;

import java.math.BigDecimal;
import java.util.Arrays;
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
import com.example.pizza.entity.Product;
import com.example.pizza.enums.PaymentMethod;
import com.example.pizza.enums.ProductType;
import com.example.pizza.model.CartItem;
import com.example.pizza.model.CheckoutForm;
import com.example.pizza.service.CartService;
import com.example.pizza.service.ComboService;
import com.example.pizza.service.CouponService;
import com.example.pizza.service.ProductService;
import com.example.pizza.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ComboService comboService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CouponService couponService;

    @PostMapping("/add")
    public String addProduct(
            @RequestParam(required = false) Integer crustId,
            @RequestParam(required = false) String key,
            @RequestParam Integer productId,
            @RequestParam(required = false) Integer quantity) {
        System.out.println("giá trị của key " + key);
        if (key.isEmpty() || key.isEmpty()) {
            cartService.save(productId, crustId, null, null, quantity);
            return "redirect:/menu";
        } else {
            cartService.update(productId, crustId, null, null, quantity, key);
            return "redirect:/cart";
        }

    }

    @PostMapping("/combo={id}/select")
    public String addCombo(@PathVariable Integer id,
            @RequestParam String[] selectedProducts,
            @RequestParam int quantity,
            @RequestParam(required = false) String key, // Đổi Integer thành String
            Model model) {
        List<Integer> productIds = Arrays.stream(selectedProducts)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if (key == null || key.isEmpty()) {
            cartService.save(null, null, id, productIds, quantity);
        } else {
            cartService.update(null, null, id, productIds, quantity, key);
        }

        return "redirect:/menu";
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        model.addAttribute("productDTOs", cartService.getAllProducts());
        model.addAttribute("comboDTOs", cartService.getAllComboDTOs());
        return "customer/cart/cart-view";
    }

    @GetMapping("/remove")
    public String removeFromCart(@RequestParam String key) { // Đổi Integer thành String
        cartService.removeItem(key);
        return "redirect:/cart";
    }

    @PostMapping("/edit")
    public String editProduct(
            @RequestParam String key, // Đổi Integer thành CartKey
            Model model) {
        System.out.println("Key item to edit" + key);
        CartItem itemToEdit = cartService.getItem(key);

        if (itemToEdit == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        model.addAttribute("key", key);
        if (itemToEdit.getComboId() == null) {
            Product product = productService.getProductById(itemToEdit.getProductId());
            model.addAttribute("product", product);
            model.addAttribute("crustPrices", productService.getAllCrustPriceMapBySize());
            // model.addAttribute("check", itemToEdit.getCrustPrice().getId());
            model.addAttribute("quantity", itemToEdit.getQuantity());

            return "customer/menu/customize-product";
        }
        Combo combo = comboService.getComboById(itemToEdit.getComboId());

        // Load danh sách sản phẩm cho combo kèm thông tin maxQuantity
        Map<ProductType, Object> productOptions = comboService.getComboOptionsWithQuantities(combo);
        model.addAttribute("combo", combo);
        model.addAttribute("productOptions", productOptions);

        // List<Integer> selectedProductIds = itemToEdit.getProductList().stream()
        // .map(CartItem::getId)
        // .collect(Collectors.toList());
        // model.addAttribute("selectedProductIds", selectedProductIds);

        return "customer/menu/combo-options";
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model) {
        // Lấy đối tượng CheckoutForm từ flash attribute
        CheckoutForm checkoutForm = (CheckoutForm) model.asMap().get("checkoutForm");

        // Nếu không có, tạo một đối tượng mới
        if (checkoutForm == null) {
            checkoutForm = cartService.prepareCheckoutForm();
        }
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("checkoutForm", checkoutForm);
        return "customer/cart/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute CheckoutForm checkoutForm, Model model) {
        try {
            BigDecimal finalAmount = couponService.calculateFinalAmountByCoupon(checkoutForm.getCoupon(),
                    cartService.getTotalAmount());
            model.addAttribute("finalAmount", finalAmount);

            return "redirect:/payment/create";
        } catch (IllegalArgumentException e) {
            model.addAttribute("success", true);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "customer/cart/checkout";
    }

}
