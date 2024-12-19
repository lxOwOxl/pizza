package com.example.pizza.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pizza.entity.User;
import com.example.pizza.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User()); // Tạo mới đối tượng User và đưa vào model

        return "user/login"; // Trả về trang login
    }

    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        System.out.println("First user: " + users.get(1).getUsername());
        model.addAttribute("users", users);
        return "admin/user/manage-users"; // Trỏ đến file Thymeleaf `manage-users.html`
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            @RequestParam String confirmPassword,
            Model model) {

        // Kiểm tra nếu có lỗi từ Validation
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        // Kiểm tra mật khẩu khớp nhau
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Passwords do not match!");
            return "user/register";
        }

        // Kiểm tra username đã tồn tại
        if (userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("usernameError", "Username already exists!");
            return "user/register";
        }

        // Kiểm tra email đã tồn tại
        if (userService.isEmailTaken(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists!");
            return "user/register";
        }

        // Đăng ký người dùng
        userService.registerUser(user);

        return "redirect:/login?registered";
    }
}
