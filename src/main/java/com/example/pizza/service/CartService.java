package com.example.pizza.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.example.pizza.model.Cart;
import com.example.pizza.model.CartItem;
import com.example.pizza.repository.CrustPriceRepository;
import com.example.pizza.repository.ProductPriceRepository;

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

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems().values()) {
            total = total.add(cartItem.getTotalPrice());
        }
        return total;
    }

    public void addCombo(int id, List<Integer> productIds, int quantity) {
        // Lấy combo từ database
        Combo combo = comboService.getComboById(id);
        CartItem cartItem = new CartItem();
        cartItem.setId(combo.getId());
        cartItem.setName(combo.getName());
        cartItem.setImage(combo.getImage());
        List<CartItem> cartItems = new ArrayList<CartItem>();
        for (Integer productId : productIds) {
            // Lấy thông tin sản phẩm từ productId
            Product product = productService.getProductById(productId);

            // Mặc định số lượng là 1
            int quantityPro = 1;

            // Tạo CartItem mới từ sản phẩm và các thông tin liên quan (Size, Crust, v.v.)
            CartItem cartItemChild = createCartItem(product, combo.getSize(), Crust.TRADITIONAL, quantityPro);

            // Kiểm tra xem sản phẩm này có đã tồn tại trong cartItems chưa
            boolean itemExists = false;
            for (CartItem existingItem : cartItems) {
                if (existingItem.getId() == cartItemChild.getId()) {

                    existingItem.setQuantity(existingItem.getQuantity() + quantityPro);
                    itemExists = true;
                    break; // Dừng vòng lặp khi đã tìm thấy sản phẩm trùng
                }
            }

            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới vào danh sách cartItems
            if (!itemExists) {
                cartItems.add(cartItemChild);
            }
        }
        cartItem.setProductList(cartItems);
        cartItem.setQuantity(quantity);
        for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
            CartItem existingItem = entry.getValue();
            int existKey = entry.getKey();
            printProductIds(existingItem.getProductList());
            printProductIds(cartItem.getProductList());
            if (existingItem.getType() == null) {
                System.out.println(existingItem.getProductList().equals(cartItem.getProductList()));
                if (existingItem.getId() == cartItem.getId()
                        && existingItem.getProductList().equals(cartItem.getProductList())) {
                    existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                    existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
                    cart.getItems().put(existKey, existingItem);
                    return;

                }
            }

        }
        cartItem.setTotalPrice(calculateCartItemPrice(cartItem));
        cart.getItems().put(Cart.counter++, cartItem);

    }

    public void printProductIds(List<CartItem> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
            return;
        }

        System.out.println("Danh sách productId:");
        products.stream()
                .map(CartItem::getId) // Lấy id của từng product
                .forEach(System.out::println); // In từng id ra màn hình
    }

    public void updateCombo(int id, List<Integer> productIds, int quantity, int keyToEdit) {

        // Lấy combo từ database
        Combo combo = comboService.getComboById(id);
        CartItem itemToEdit = new CartItem();
        itemToEdit.setId(combo.getId());
        itemToEdit.setName(combo.getName());
        itemToEdit.setImage(combo.getImage());
        List<CartItem> itemChildList = new ArrayList<CartItem>();
        for (Integer productId : productIds) {
            // Lấy thông tin sản phẩm từ productId
            Product product = productService.getProductById(productId);

            // Mặc định số lượng là 1
            int quantityPro = 1;

            // Tạo CartItem mới từ sản phẩm và các thông tin liên quan (Size, Crust, v.v.)
            CartItem itemToEditChild = createCartItem(product, combo.getSize(), Crust.TRADITIONAL, quantityPro);

            // Kiểm tra xem sản phẩm này có đã tồn tại trong itemChildList chưa
            boolean itemExists = false;
            for (CartItem existingItem : itemChildList) {
                if (existingItem.getId() == itemToEditChild.getId()) {

                    existingItem.setQuantity(existingItem.getQuantity() + quantityPro);
                    itemExists = true;
                    break; // Dừng vòng lặp khi đã tìm thấy sản phẩm trùng
                }
            }

            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới vào danh sách
            // itemChildList
            if (!itemExists) {
                itemChildList.add(itemToEditChild);
            }
        }
        itemToEdit.setProductList(itemChildList);
        for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
            CartItem existingItem = entry.getValue();
            int existKey = entry.getKey();
            printProductIds(existingItem.getProductList());
            printProductIds(itemToEdit.getProductList());
            if (existKey == keyToEdit)
                continue;
            if (existingItem.getType() == null) {
                System.out.println(existingItem.getProductList().equals(itemToEdit.getProductList()));
                if (existingItem.getId() == itemToEdit.getId()
                        && existingItem.getProductList().equals(itemToEdit.getProductList())) {
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    existingItem.setTotalPrice(calculateCartItemPrice(existingItem));
                    cart.getItems().put(existKey, existingItem);
                    cart.getItems().remove(keyToEdit);

                    return;

                }
            }
        }
        itemToEdit.setQuantity(quantity);
        itemToEdit.setTotalPrice(calculateCartItemPrice(itemToEdit));
        cart.getItems().put(keyToEdit, itemToEdit);

    }

    private CartItem createCartItem(Product product, Size size, Crust crust, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setId(product.getId());
        cartItem.setName(product.getName());
        cartItem.setImage(product.getImage());
        cartItem.setType(product.getType());
        if (product.getType() == ProductType.PIZZA) {
            cartItem.setCrust(crust);
            cartItem.setSize(size);
        }
        return cartItem;
    }

    public boolean equals(CartItem cartItem1, CartItem cartItem2) {
        if (cartItem1.getCrust() == null || cartItem2.getCrust() == null || cartItem1.getSize() == null
                || cartItem2.getSize() == null)
            return false;
        return cartItem1.getId() == cartItem2.getId()
                && cartItem1.getSize().equals(cartItem2.getSize())
                && cartItem1.getCrust().equals(cartItem2.getCrust());
    }

    public void addItem(int productId, Size size, Crust crust, int quantity) {
        Product product = productService.getProductById(productId);
        CartItem itemToAdd = new CartItem(productId, product.getName(), product.getImage(), size, crust,
                product.getType(), quantity);
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

    public void updateItem(int productId, Size size, Crust crust, int quantity, Integer keyToEdit) {

        Product product = productService.getProductById(productId);

        CartItem ItemToEdit = new CartItem(productId, product.getName(), product.getImage(), size, crust,
                product.getType(), quantity);
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
        if (cartItem.getType() == null) {
            return comboService.getPriceById(cartItem.getId()).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        }
        BigDecimal addCrustPrice = BigDecimal.ZERO;
        if (cartItem.getType() == ProductType.PIZZA) {
            addCrustPrice = crustPriceRepository.findPriceBySizeAndCrust(cartItem.getSize(), cartItem.getCrust());
        }
        BigDecimal basePrice = productPriceRepository.findPriceByProductIdAndSize(cartItem.getId(), cartItem.getSize());
        return basePrice.add(addCrustPrice).multiply(BigDecimal.valueOf(cartItem.getQuantity()));

    }

}
