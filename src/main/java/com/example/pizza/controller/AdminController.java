package com.example.pizza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;
import com.example.pizza.entity.User;
import com.example.pizza.enums.Category;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.example.pizza.service.FileSystemStorageService;
import com.example.pizza.service.OrderService;
import com.example.pizza.service.ProductService;
import com.example.pizza.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    // 1. Hiển thị trang Admin Dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard"; // Trả về file HTML dashboard
    }

    // 2. Quản lý sản phẩm - Hiển thị danh sách sản phẩm
    @GetMapping("/products")
    public String listProducts(Model model) throws JsonProcessingException {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("types", ProductType.values());
        model.addAttribute("products", products);

        return "admin/products";
    }

    // 3. Thêm sản phẩm mới - Hiển thị form
    @GetMapping("/product/form")
    public String showForm(@RequestParam(required = false) ProductType type,
            @RequestParam(required = false) Integer id,
            Model model) {
        if (id == null) {
            Product product = new Product();
            product.setType(type);
            model.addAttribute("product", product);
            model.addAttribute("type", type);

        } else {
            Product product = productService.getProductById(id);
            type = product.getType();

            model.addAttribute("type", type);
            model.addAttribute("product", product);
        }

        model.addAttribute("sizes", Size.values());

        model.addAttribute("categories", Category.getCategoriesByType(type.name()));

        return "admin/product-add";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product,
            @RequestParam MultipartFile imageFile) {

        if (!imageFile.isEmpty()) {
            String fileName = fileSystemStorageService.store(imageFile);
            product.setImage(fileName);
        }

        for (ProductPrice price : product.getPrices()) {
            if (price.getSize() == null)
                System.out.println("price.getSize()");
            price.setProduct(product);
        }
        productService.save(product);
        return "redirect:/admin/products";
    }

    // // 4. Thêm sản phẩm mới - Xử lý dữ liệu
    // @PostMapping("/products")
    // public String saveProduct(@ModelAttribute Product product) {
    // productService.saveProduct(product);
    // return "redirect:/admin/products";
    // }

    // // 6. Sửa sản phẩm - Xử lý dữ liệu cập nhật
    @PostMapping("/product/edit")
    public String updateProduct(@ModelAttribute Product product) {

        productService.update(product);
        return "redirect:/admin/products";
    }

    // // 7. Xóa sản phẩm
    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam int id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    // // 8. Quản lý đơn hàng - Hiển thị danh sách đơn hàng
    // @GetMapping("/orders")
    // public String listOrders(Model model) {
    // List<Order> orders = orderService.getAllOrders();
    // model.addAttribute("orders", orders);
    // return "admin/orders";
    // }

    // // 9. Quản lý người dùng - Hiển thị danh sách người dùng
    // @GetMapping("/users")
    // public String listUsers(Model model) {
    // List<User> users = userService.getAllUsers();
    // model.addAttribute("users", users);
    // return "admin/users";
    // }
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        System.out.println("First user: " + users.get(1).getUsername());
        model.addAttribute("users", users);
        return "admin/admin-users-view";
    }
}
