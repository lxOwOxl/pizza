package com.example.pizza.service;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.example.pizza.model.Cart;
import com.example.pizza.model.CartItem;
import com.example.pizza.model.CartKey;
import com.example.pizza.model.CheckoutForm;
import com.example.pizza.model.ComboDTO;
import com.example.pizza.model.ProductDTO;
import com.example.pizza.model.UserDTO;
import com.example.pizza.repository.CouponRepository;
import com.example.pizza.repository.CrustPriceRepository;
import com.example.pizza.repository.ProductPriceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CrustPriceRepository crustPriceRepository;
    @Autowired
    private ComboService comboService;
    @Autowired
    private Cart cart;
    @Autowired
    private UserService userService;

    @Value("${delivery.fee.default}")
    private BigDecimal defaultShippingFee;

    public CheckoutForm prepareCheckoutForm() {
        CheckoutForm checkoutForm = new CheckoutForm();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            UserDTO userDTO = UserDTO.fromUser(userService.getUserByUserName(username));
            checkoutForm.setUserDTO(userDTO);
        }
        checkoutForm.setTotalAmount(getTotalAmount());
        checkoutForm.setShippingFee(defaultShippingFee);
        checkoutForm.setFinalAmount(getTotalAmount().add(defaultShippingFee));
        return checkoutForm;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        Map<String, ProductDTO> productDTOs = getAllProducts();
        for (ProductDTO productDTO : productDTOs.values()) {
            total = total.add(productDTO.getPrice());
        }
        Map<String, ComboDTO> comboDTOs = getAllComboDTOs();
        for (ComboDTO comboDTO : comboDTOs.values()) {
            total = total.add(comboDTO.getPrice());
        }
        return total;
    }

    public void save(Integer productId, Integer crustId, Integer comboId, List<Integer> productIds, int quantity) {
        String cartKey = generateKey(productId, crustId, comboId, productIds);

        if (cart.getItems().containsKey(cartKey)) {
            CartItem existingItem = cart.getItems().get(cartKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem itemToAdd = new CartItem(productId, comboId, crustId, quantity, productIds);
            cart.getItems().put(cartKey, itemToAdd);
        }
    }

    public void update(Integer productId, Integer crustId, Integer comboId, List<Integer> productIds, int quantity,
            String editKey) {

        String newKey = generateKey(productId, crustId, comboId, productIds);
        if (editKey.equals(newKey)) {
            CartItem existingItem = cart.getItems().get(newKey);
            existingItem.setQuantity(quantity);
            cart.getItems().put(newKey, existingItem);
        } else if (cart.getItems().containsKey(newKey)) {
            CartItem existingItem = cart.getItems().get(newKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cart.getItems().remove(editKey);
        } else {
            CartItem newItem = new CartItem(productId, comboId, crustId, quantity, productIds);
            cart.getItems().put(newKey, newItem);
            cart.getItems().remove(editKey);
        }
    }

    public boolean removeItem(String key) {
        return cart.getItems().remove(key) != null;
    }

    public Map<String, CartItem> getItems() {
        return cart.getItems();
    }

    public CartItem getItem(String key) {
        return cart.getItems().get(key);
    }

    private String generateKey(Integer productId, Integer crustId, Integer comboId, List<Integer> productIds) {
        String str = "";
        if (productIds != null) {
            for (Integer id : productIds) {
                str = "-" + id;
            }
        }
        return productId + "-" + crustId + "-" + comboId + "-" + str;
    }

    public Map<String, ProductDTO> getAllProducts() {
        Map<String, ProductDTO> productDTOs = new HashMap<>();
        // Duyệt qua các entry của cart.getItems()
        for (Map.Entry<String, CartItem> entry : cart.getItems().entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem.getProductId() != null) {
                ProductDTO productDTO = productService.getProductDTO(cartItem.getProductId(), cartItem.getCrustId(),
                        cartItem.getQuantity());
                // Sử dụng productId làm key trong map productDTOs
                productDTOs.put(entry.getKey(), productDTO);
            }
        }
        return productDTOs;
    }

    public Map<String, ComboDTO> getAllComboDTOs() {
        Map<String, ComboDTO> comboDTOs = new HashMap<>();
        // Duyệt qua các entry của cart.getItems()
        for (Map.Entry<String, CartItem> entry : cart.getItems().entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem.getComboId() != null) {
                ComboDTO comboDTO = comboService.getComboDTO(cartItem.getComboId(), cartItem.getProductIds());
                comboDTOs.put(entry.getKey(), comboDTO);
            }
        }
        return comboDTOs;
    }

}
