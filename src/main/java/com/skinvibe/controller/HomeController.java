package com.skinvibe.controller;

import com.skinvibe.model.Product;
import com.skinvibe.service.CategoryService;
import com.skinvibe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping({"/", "/home"})
    public String home(Model model, 
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "8") int size) {
        
        // Get featured products
        List<Product> featuredProducts = productService.getFeaturedProducts();
        model.addAttribute("featuredProducts", featuredProducts);
        
        // Get all active products with pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllActiveProducts(pageable);
        model.addAttribute("products", products);
        
        // Get all active categories
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        
        return "home";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String q, 
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "8") int size,
                        Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(q, pageable);
        
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", q);
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        
        return "search";
    }
}
