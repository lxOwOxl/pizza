package com.example.pizza.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class Cart {
    private Map<Integer, CartItem> items = new HashMap<>();

    public static int counter = 0;

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public CartItem getItem(Integer key) {
        return items.get(key);
    }

    public void setItems(Map<Integer, CartItem> items) {
        this.items = items;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Cart.counter = counter;
    }

}
