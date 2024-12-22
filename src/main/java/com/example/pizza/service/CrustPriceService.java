package com.example.pizza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pizza.entity.CrustPrice;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;
import com.example.pizza.repository.CrustPriceRepository;

@Service
public class CrustPriceService {

    @Autowired
    private CrustPriceRepository crustPriceRepository;

    // Lấy danh sách tất cả CrustPrice
    public List<CrustPrice> getAll() {
        return crustPriceRepository.findAll();
    }

    // Lấy CrustPrice theo ID
    public CrustPrice getById(Integer id) {
        return crustPriceRepository.findById(id).orElse(null);
    }

    // Lưu hoặc cập nhật CrustPrice
    public CrustPrice save(CrustPrice crustPrice) {
        if (crustPriceRepository.existsBySizeAndCrust(crustPrice.getSize(), crustPrice.getCrust())) {
            return null;
        }
        return crustPriceRepository.save(crustPrice);
    }

    // Xóa CrustPrice theo ID
    public void delete(Integer id) {
        crustPriceRepository.deleteById(id);
    }

}
