package com.skinvibe.controller;

import com.skinvibe.model.*;
import com.skinvibe.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    // Admin Dashboard
    @GetMapping
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        // Get statistics for dashboard
        long totalProducts = productService.getTotalProducts();
        long totalOrders = orderService.getTotalOrders();
        long totalUsers = userService.getTotalUsers();
        long pendingOrders = orderService.getPendingOrdersCount();
        
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("pendingOrders", pendingOrders);
        
        return "admin/dashboard";
    }
    
    // Product Management
    @GetMapping("/products")
    public String adminProducts(HttpSession session, Model model, 
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageable);
        
        model.addAttribute("products", products);
        return "admin/products/list";
    }
    
    @GetMapping("/products/add")
    public String addProductForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/form";
    }
    
    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute Product product, 
                           BindingResult result,
                           HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/products/form";
        }
        
        // Set default image if none provided
        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl("/images/placeholder-product.jpg");
        }
        
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }
    
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/admin/products";
        }
        
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/form";
    }
    
    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, 
                            BindingResult result,
                            HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/products/form";
        }
        
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return "redirect:/admin/products";
        }
        
        // Keep existing image if no new URL provided
        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl(existingProduct.getImageUrl());
        }
        
        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }
    
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
    
    // Order Management
    @GetMapping("/orders")
    public String adminOrders(HttpSession session, Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getAllOrders(pageable);
        
        model.addAttribute("orders", orders);
        return "admin/orders/list";
    }
    
    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/admin/orders";
        }
        
        model.addAttribute("order", order);
        return "admin/orders/detail";
    }
    
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, 
                                  @RequestParam Order.OrderStatus status,
                                  HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        Order order = orderService.getOrderById(id);
        if (order != null) {
            order.setOrderStatus(status);
            orderService.saveOrder(order);
        }
        
        return "redirect:/admin/orders/" + id;
    }
    
    @PostMapping("/orders/{id}/payment-status")
    public String updatePaymentStatus(@PathVariable Long id, 
                                    @RequestParam Order.PaymentStatus status,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(User.Role.ADMIN)) {
            return "redirect:/auth/login";
        }
        
        Order order = orderService.getOrderById(id);
        if (order != null) {
            order.setPaymentStatus(status);
            orderService.saveOrder(order);
        }
        
        return "redirect:/admin/orders/" + id;
    }
}
