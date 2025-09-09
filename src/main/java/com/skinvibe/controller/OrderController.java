package com.skinvibe.controller;

import com.skinvibe.model.*;
import com.skinvibe.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private AddressService addressService;
    
    @GetMapping
    public String orders(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session,
                        Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getOrdersByUser(user, pageable);
        
        model.addAttribute("orders", orders);
        return "orders/list";
    }
    
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if user owns this order
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        model.addAttribute("order", order);
        return "orders/detail";
    }
    
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<CartItem> cartItems = cartService.getCartItems(user);
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }
        
        List<Address> addresses = addressService.getAddressesByUser(user);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        model.addAttribute("addresses", addresses);
        model.addAttribute("order", new Order());
        
        return "orders/checkout";
    }
    
    @PostMapping("/place")
    public String placeOrder(@ModelAttribute Order order,
                            @RequestParam Long shippingAddressId,
                            @RequestParam(required = false) Long billingAddressId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
            
            Address shippingAddress = addressService.findById(shippingAddressId)
                    .orElseThrow(() -> new RuntimeException("Shipping address not found"));
            
            Address billingAddress = billingAddressId != null ? 
                    addressService.findById(billingAddressId).orElse(shippingAddress) : 
                    shippingAddress;
            
            Order createdOrder = orderService.createOrder(user, shippingAddress, billingAddress, 
                    order.getPaymentMethod(), order.getNotes());
            
            redirectAttributes.addFlashAttribute("success", "Order placed successfully! Order #: " + createdOrder.getOrderNumber());
            return "redirect:/orders/" + createdOrder.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/checkout";
        }
    }
}
