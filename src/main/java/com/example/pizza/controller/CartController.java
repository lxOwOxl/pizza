package com.example.pizza.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.pizza.model.UserDTO;
import com.example.pizza.service.CartService;
import com.example.pizza.service.ComboService;
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

    @PostMapping("/add")
    public String addProduct(
            @RequestParam int productId, @RequestParam(required = false) Size size,
            @RequestParam(required = false) Crust crust,
            @RequestParam int quantity,
            @RequestParam(required = false) Integer key) {
        if (key != null) {
            cartService.updateItem(productId, size, crust, quantity, key);
        } else
            cartService.addItem(productId, size, crust, quantity);

        return "redirect:/menu";
    }

    @PostMapping("/combo={id}/select")
    public String addCombo(@PathVariable Integer id,
            @RequestParam String[] selectedProducts,
            @RequestParam int quantity,
            @RequestParam(required = false) Integer key,
            Model model) {
        List<Integer> productIds = Arrays.stream(selectedProducts)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if (key != null) {
            cartService.updateCombo(id, productIds, quantity, key);
        } else
            cartService.addCombo(id, productIds, quantity);

        return "redirect:/menu";
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        model.addAttribute("cartItems", cartService.getItems());
        return "customer/cart/cart-view";
    }

    @GetMapping("/remove")
    public String removeFromCart(@RequestParam int key) {
        cartService.removeItem(key);
        return "redirect:/cart";

    }

    @PostMapping("/edit")
    public String editProduct(
            @RequestParam Integer key,
            Model model) {
        System.out.println("Key item to edit" + key);
        CartItem itemToEdit = cartService.getItem(key);

        if (itemToEdit == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        model.addAttribute("key", key);
        if (itemToEdit.getType() == null) {
            List<ProductPrice> productPrices = productService.getPriceListByProduct(itemToEdit.getId());
            Product product = productService.getProductById(itemToEdit.getId());
            model.addAttribute("product", product);
            model.addAttribute("quantity", itemToEdit.getQuantity());
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
        Combo combo = comboService.getComboById(itemToEdit.getId());

        // Load danh sách sản phẩm cho combo kèm thông tin maxQuantity
        Map<ProductType, Object> productOptions = comboService.getComboOptionsWithQuantities(combo);
        model.addAttribute("combo", combo);
        // Đưa dữ liệu vào Model
        model.addAttribute("productOptions", productOptions);

        List<Integer> selectedProductIds = itemToEdit.getProductList().stream()
                .map(CartItem::getId) // Lấy thuộc tính id từ Product
                .collect(Collectors.toList());
        model.addAttribute("selectedProductIds", selectedProductIds);
        // Trả về tên View (HTML file)
        return "customer/menu/combo-options";

    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model) {

        UserDTO userDTO = new UserDTO();
        // Lấy thông tin người dùng đăng nhập (nếu có)
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated()
        // && !(authentication instanceof AnonymousAuthenticationToken)) {
        // // Lấy email hoặc username từ thông tin đăng nhập
        // String username = authentication.getName();

        // // Tìm người dùng trong cơ sở dữ liệu
        // User user = userService.findByEmailOrUsername(username); // Cần triển khai
        // phương thức này

        // if (user != null) {
        // // Gán thông tin từ User vào UserDTO
        // userDTO.setFullName(user.getFullName());
        // userDTO.setEmail(user.getEmail());
        // userDTO.setPhone(user.getPhone());
        // userDTO.setAddress(user.getAddress());
        // }
        // }

        // Thêm UserDTO và tổng tiền vào model
        model.addAttribute("userDTO", userDTO);
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
