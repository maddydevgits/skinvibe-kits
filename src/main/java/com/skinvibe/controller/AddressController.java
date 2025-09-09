package com.skinvibe.controller;

import com.skinvibe.model.Address;
import com.skinvibe.model.User;
import com.skinvibe.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    @GetMapping
    public String addresses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<Address> addresses = addressService.getAddressesByUser(user);
        
        model.addAttribute("addresses", addresses);
        model.addAttribute("address", new Address());
        
        return "addresses/list";
    }
    
    @GetMapping("/add")
    public String addAddress(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("address", new Address());
        return "addresses/form";
    }
    
    @PostMapping("/add")
    public String saveAddress(@Valid @ModelAttribute Address address,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "addresses/form";
        }
        
        try {
            User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
            address.setUser(user);
            addressService.saveAddress(address);
            redirectAttributes.addFlashAttribute("success", "Address saved successfully!");
            return "redirect:/addresses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save address: " + e.getMessage());
            return "addresses/form";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String editAddress(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Address address = addressService.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        // Check if user owns this address
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        model.addAttribute("address", address);
        return "addresses/form";
    }
    
    @PostMapping("/edit/{id}")
    public String updateAddress(@PathVariable Long id,
                               @Valid @ModelAttribute Address address,
                               BindingResult result,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "addresses/form";
        }
        
        try {
            User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
            Address existingAddress = addressService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            
            // Check if user owns this address
            if (!existingAddress.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied");
            }
            
            address.setId(id);
            address.setUser(user);
            addressService.updateAddress(address);
            redirectAttributes.addFlashAttribute("success", "Address updated successfully!");
            return "redirect:/addresses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update address: " + e.getMessage());
            return "addresses/form";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
            Address address = addressService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            
            // Check if user owns this address
            if (!address.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied");
            }
            
            addressService.deleteAddress(id);
            redirectAttributes.addFlashAttribute("success", "Address deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete address: " + e.getMessage());
        }
        
        return "redirect:/addresses";
    }
}
