package com.example.pizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.Combo;
import com.example.pizza.repository.ComboItemRepository;
import com.example.pizza.repository.ComboRepository;

import java.util.List;

@Service
public class ComboService {
    @Autowired
    private final ComboRepository comboRepository;
    @Autowired
    private ComboItemRepository comboItemRepository;

    public ComboService(ComboRepository comboRepository) {
        this.comboRepository = comboRepository;
    }

    // Lấy danh sách tất cả combo
    public List<Combo> getAllCombos() {
        return comboRepository.findAll();
    }

    // Lấy thông tin combo theo ID
    public Combo getComboById(Integer id) {
        return comboRepository.findById(id).orElse(null);
    }

    // Thêm hoặc cập nhật combo
    public Combo saveCombo(Combo combo) {
        return comboRepository.save(combo);
    }

    // Xóa combo theo ID
    public void deleteCombo(Integer id) {
        comboRepository.deleteById(id);
    }

}
