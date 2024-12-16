package com.example.pizza.service;

import org.springframework.stereotype.Service;

import com.example.pizza.entity.ComboItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComboItemService {

    private final List<ComboItem> comboItems = new ArrayList<>();

    public List<ComboItem> getAllComboItems() {
        return comboItems;
    }

    public ComboItem getComboItemById(Integer id) {
        return comboItems.stream()
                .filter(comboItem -> comboItem.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public ComboItem createComboItem(ComboItem comboItem) {
        comboItems.add(comboItem);
        return comboItem;
    }

    public void deleteComboItem(Integer id) {
        comboItems.removeIf(comboItem -> comboItem.getId().equals(id));
    }
}