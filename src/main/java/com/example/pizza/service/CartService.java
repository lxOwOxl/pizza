package com.example.pizza.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.enums.ProductType;
import com.example.pizza.model.Cart;
import com.example.pizza.model.CartItem;
import com.example.pizza.repository.CrustPriceRepository;
import com.example.pizza.repository.ProductPriceRepository;

@Service
public class CartService {
    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private CrustPriceRepository crustPriceRepository;
    @Autowired
    private Cart cart;

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems().values()) {
            total = total.add(cartItem.getTotalPrice());
        }
        return total;
    }

    public boolean equals(CartItem cartItem1, CartItem cartItem2) {
        if (cartItem1.getCrust() == null || cartItem2.getCrust() == null || cartItem1.getSize() == null
                || cartItem2.getSize() == null)
            return false;
        return cartItem1.getId() == cartItem2.getId()
                && cartItem1.getSize().equals(cartItem2.getSize())
                && cartItem1.getCrust().equals(cartItem2.getCrust());
    }

    public void addCartItem(CartItem itemToAdd) {

        for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
            CartItem existingItem = entry.getValue();
            int existKey = entry.getKey();

            if (equals(existingItem, itemToAdd)
                    || (itemToAdd.getType() != ProductType.PIZZA) && existingItem.getId() == itemToAdd.getId()) {
                existingItem.setQuantity(itemToAdd.getQuantity() +
                        existingItem.getQuantity());
                existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
                cart.getItems().put(existKey, existingItem);
                return;
            }
        }
        itemToAdd.setTotalPrice(calculateCartItemPrice(itemToAdd));
        cart.getItems().put(Cart.counter++, itemToAdd);

    }

    public void updateItem(CartItem ItemToEdit, Integer keyToEdit) {
        for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
            CartItem existingItem = entry.getValue();
            int existKey = entry.getKey();
            if (existKey == keyToEdit)
                continue;
            if (equals(existingItem, ItemToEdit)) {
                existingItem.setQuantity(ItemToEdit.getQuantity() +
                        existingItem.getQuantity());
                existingItem.setTotalPrice(calculateCartItemPrice(existingItem));

                cart.getItems().put(existKey, existingItem);
                cart.getItems().remove(keyToEdit);
                return;
            }
        }

        ItemToEdit.setTotalPrice(calculateCartItemPrice(ItemToEdit));
        cart.getItems().put(keyToEdit, ItemToEdit);

    }

    public boolean removeItem(int key) {
        return cart.getItems().remove(key) != null;
    }

    public Map<Integer, CartItem> getItems() {
        return cart.getItems();
    }

    public CartItem getItem(int key) {
        return cart.getItems().get(key);
    }

    public BigDecimal calculateCartItemPrice(CartItem cartItem) {

        BigDecimal basePrice = cartItem.getSize() != null
                ? productPriceRepository.findPriceByProductIdAndSize(cartItem.getId(), cartItem.getSize())
                : productPriceRepository.findPriceByProductId(cartItem.getId());
        BigDecimal add_crust_price = cartItem
                .getSize() != null ? crustPriceRepository.findPriceBySizeAndCrust(cartItem.getSize(),
                        cartItem.getCrust()) : BigDecimal.ZERO;
        return basePrice.add(add_crust_price).multiply(BigDecimal.valueOf(cartItem.getQuantity()));

    }

}
