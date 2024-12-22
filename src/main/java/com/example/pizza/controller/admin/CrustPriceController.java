package com.example.pizza.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pizza.entity.CrustPrice;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;
import com.example.pizza.service.CrustPriceService;
import com.example.pizza.service.ProductService;

@Controller
@RequestMapping("/admin/crust-price")
public class CrustPriceController {

    @Autowired
    private CrustPriceService crustPriceService;

    // Thêm mới CrustPrice
    @GetMapping("/add")
    public String addCrustPriceForm(@RequestParam(required = false) Integer id, Model model) {
        CrustPrice crustPrice = new CrustPrice();
        if (id != null) {
            crustPrice = crustPriceService.getById(id);
        }
        model.addAttribute("crustPrice", crustPrice);
        model.addAttribute("sizes", Size.values());
        model.addAttribute("crusts", Crust.values());
        return "admin/crust-price-form";
    }

    @PostMapping("/add")
    public String addCrustPrice(@ModelAttribute CrustPrice crustPrice, RedirectAttributes redirectAttributes) {

        if (crustPriceService.save(crustPrice) == null) {
            redirectAttributes.addFlashAttribute("message",
                    "Đã tồn tại " + crustPrice.getCrust() + " - " + crustPrice.getSize());
            return "redirect:/admin/crust-price/add";
        }
        ;
        return "redirect:/admin/products";
    }

    // Chỉnh sửa CrustPrice
    @GetMapping("/edit/{id}")
    public String editCrustPriceForm(@PathVariable Integer id, Model model) {
        CrustPrice crustPrice = crustPriceService.getById(id);
        model.addAttribute("crustPrice", crustPrice);
        model.addAttribute("sizes", Size.values());
        model.addAttribute("crusts", Crust.values());
        return "admin/crust-price/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCrustPrice(@PathVariable Integer id, @ModelAttribute CrustPrice crustPrice) {
        crustPrice.setId(id);
        crustPriceService.save(crustPrice);
        return "redirect:/admin/crust-price/list";
    }

    // Xóa CrustPrice
    @PostMapping("/delete/{id}")
    public String deleteCrustPrice(@PathVariable Integer id) {
        crustPriceService.delete(id);
        return "redirect:/admin/crust-price/list";
    }
}
