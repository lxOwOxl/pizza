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
        for (CartItem cartItem : cart.getItems().values()) {
            total = total.add(cartItem.getTotalPrice());
        }
        return total;
    }

    public void save(int productId, Integer crustId, int quantity) {
        ProductDTO productDTO = productService.getProductDTO(productId, crustId);

        CartItem itemToAdd = new CartItem(productDTO, quantity);

        String cartKey = generateKey(itemToAdd);
        if (cart.getItems().containsKey(cartKey)) {
            CartItem existingItem = cart.getItems().get(cartKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
        } else {
            itemToAdd.setTotalPrice(calculateCartItemPrice(itemToAdd));
            cart.getItems().put(cartKey, itemToAdd);
        }
    }

    public void update(int productId, Integer crustId, int quantity, String keyToEdit) {
        ProductDTO productDTO = productService.getProductDTO(productId, crustId);
        CartItem newItem = new CartItem(productDTO, quantity);

        String newKey = generateKey(newItem);

        // Kiểm tra xem key đang chỉnh sửa có giống với key mới không
        if (keyToEdit.equals(newKey)) {
            newItem.setTotalPrice(calculateCartItemPrice(newItem));
            cart.getItems().put(newKey, newItem);

        } else if (cart.getItems().containsKey(newKey)) {
            // Nếu newKey đã tồn tại trong giỏ hàng, cập nhật số lượng và giá, sau đó xóa
            // item cũ
            CartItem existingItem = cart.getItems().get(newKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
            // Xóa item với keyToEdit nếu có trong giỏ hàng
            cart.getItems().remove(keyToEdit);
        } else {
            // Nếu newKey chưa tồn tại trong giỏ hàng, thêm item mới vào giỏ hàng
            newItem.setTotalPrice(calculateCartItemPrice(newItem));
            cart.getItems().put(newKey, newItem);
            cart.getItems().remove(keyToEdit);

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

    public void addCombo(int id, List<Integer> productIds, int quantity) {
        ComboDTO comboDTO = comboService.getComboDTO(id, productIds);
        CartItem cartItem = new CartItem(comboDTO, quantity);
        String comboKey = generateKey(cartItem);

        if (cart.getItems().containsKey(comboKey)) {
            CartItem existingItem = cart.getItems().get(comboKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
        } else {
            cartItem.setTotalPrice(calculateCartItemPrice(cartItem));
            cart.getItems().put(comboKey, cartItem);
        }
    }

    private String generateKey(CartItem cartItem) {
        if (cartItem.getComboDTO() == null) {
            return cartItem.getProductDTO().getId() + "-" + cartItem.getProductDTO()
                    .getType() + "-" + cartItem.getProductDTO().getCrust() + "-"
                    + cartItem.getProductDTO().getSize();
        }
        String str = String.valueOf(cartItem.getComboDTO().getId());
        for (ProductDTO dto : cartItem.getComboDTO().getProductDTOs()) {
            str += "-" + dto.getId();
        }
        return str;

    }

    public BigDecimal calculateCartItemPrice(CartItem cartItem) {
        if (cartItem.getComboDTO() == null) {
            return cartItem.getProductDTO().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        }

        return cartItem.getComboDTO().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    public void updateCombo(int id, List<Integer> productIds, int quantity, String keyToEdit) {
        ComboDTO comboDTO = comboService.getComboDTO(id, productIds);
        CartItem newCartItem = new CartItem(comboDTO, quantity);
        String newKey = generateKey(newCartItem);
        if (newKey.equals(keyToEdit)) {
            CartItem existingItem = cart.getItems().get(newKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
        } else if (cart.getItems().containsKey(newKey)) {
            CartItem existingItem = cart.getItems().get(newKey);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
            cart.getItems().remove(keyToEdit);
        } else {
            newCartItem.setTotalPrice(calculateCartItemPrice(newCartItem));
            cart.getItems().put(newKey, newCartItem);
        }
    }

}
