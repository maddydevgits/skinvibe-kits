package com.skinvibe.config;

import com.skinvibe.model.*;
import com.skinvibe.repository.*;
import com.skinvibe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("DataInitializer running...");
        System.out.println("User count: " + userRepository.count());
        System.out.println("Product count: " + productRepository.count());
        
        // Always check and create admin user if it doesn't exist
        ensureAdminUserExists();
        
        // Create test user if it doesn't exist
        ensureTestUserExists();
        
        // Initialize products if they don't exist
        if (productRepository.count() == 0) {
            System.out.println("No products found, initializing products...");
            initializeProducts();
        } else {
            System.out.println("Products already exist: " + productRepository.count());
        }
        
        System.out.println("DataInitializer completed. Final product count: " + productRepository.count());
    }
    
    private void ensureAdminUserExists() {
        // Check if admin user exists
        Optional<User> adminUser = userRepository.findByUsername("admin");
        
        if (!adminUser.isPresent()) {
            System.out.println("Admin user not found, creating admin user...");
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@skinvibe.com");
            admin.setPassword("admin123");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(User.Role.ADMIN);
            userService.saveUser(admin);
            System.out.println("Admin user created successfully!");
            System.out.println("Admin credentials: username=admin, password=admin123");
        } else {
            // Check if password needs to be updated (if it's not hashed)
            User admin = adminUser.get();
            if (admin.getPassword().equals("admin123")) {
                System.out.println("Admin user exists but password is not hashed, updating...");
                admin.setPassword("admin123");
                userService.saveUser(admin);
                System.out.println("Admin user password updated successfully!");
            } else {
                System.out.println("Admin user already exists with hashed password.");
            }
        }
    }
    
    private void ensureTestUserExists() {
        // Check if test user exists
        boolean testUserExists = userRepository.findByUsername("testuser").isPresent();
        
        if (!testUserExists) {
            System.out.println("Test user not found, creating test user...");
            User user = new User();
            user.setUsername("testuser");
            user.setEmail("user@skinvibe.com");
            user.setPassword("user123");
            user.setFirstName("Test");
            user.setLastName("User");
            user.setPhoneNumber("+1234567890");
            userService.saveUser(user);
            System.out.println("Test user created successfully!");
            System.out.println("Test user credentials: username=testuser, password=user123");
        } else {
            System.out.println("Test user already exists.");
        }
    }
    
    private void initializeData() {
        // This method is no longer used as we now use ensureAdminUserExists() and ensureTestUserExists()
        
        // Create categories
        Category cleansers = new Category("Cleansers", "Gentle cleansers for all skin types");
        cleansers.setImageUrl("https://oseamalibu.com/cdn/shop/files/OSEA_OceanCleanser_OC-1-DTC-1.jpg?v=1739317248&width=588");
        categoryRepository.save(cleansers);
        
        Category moisturizers = new Category("Moisturizers", "Hydrating moisturizers for healthy skin");
        moisturizers.setImageUrl("https://imgs.search.brave.com/azAWw8GjXOE7spO0w6JG0QQRpw70qsqMQLCqibhNous/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly84YzM0/MTJkNzYyMjVkMDRk/N2JhYS1iZTk4YjZl/YTE3OTIwOTUzZmI5/MzEyODJlZmY5YTY4/MS5pbWFnZXMubG92/ZWx5c2tpbi5jb20v/amx6enVoZXdfMjAy/NDA0MTYxMzA1NDU4/MzEwLmpwZw");
        categoryRepository.save(moisturizers);
        
        Category serums = new Category("Serums", "Concentrated treatments for specific skin concerns");
        serums.setImageUrl("https://www.endro-cosmetiques.com/cdn/shop/files/serum-bonne-mine-10-de-vitamine-c-endro-cosmetiques-serums-visage-573577.webp?crop=center&height=952&v=1752731978&width=952");
        categoryRepository.save(serums);
        
        Category sunscreens = new Category("Sunscreens", "Protection from harmful UV rays");
        sunscreens.setImageUrl("https://m.media-amazon.com/images/I/410l-rym1yL._SX300_SY300_QL70_FMwebp_.jpg");
        categoryRepository.save(sunscreens);
        
        Category masks = new Category("Face Masks", "Weekly treatments for deep cleansing and nourishment");
        masks.setImageUrl("https://m.media-amazon.com/images/I/61An0jiJFvL._SX679_.jpg");
        categoryRepository.save(masks);
        
        Category toners = new Category("Toners", "Balancing and refreshing toners");
        toners.setImageUrl("https://pyxis.nymag.com/v1/imgs/129/9d7/7c474d34b1ea7f68d497d02b00b596d59a-toners-7-29.2x.rhorizontal.w700.jpg");
        categoryRepository.save(toners);
        
        Category eyeCare = new Category("Eye Care", "Specialized treatments for the delicate eye area");
        eyeCare.setImageUrl("https://imgs.search.brave.com/M1mtCnajUQ7P3IPaX-8hqh6t6oo2dS8jVbv5M8JedMs/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93d3cu/YnV0dGVyZmxpZXMt/ZXllY2FyZS5jby51/ay9jZG4vc2hvcC9m/aWxlcy9leWVjYXJl/LXByZXNlbnRhdGlv/bi1wYWNrLTMuanBn/P3Y9MTcyMjY4MDk4/MSZ3aWR0aD00ODA");
        categoryRepository.save(eyeCare);
        
        Category treatments = new Category("Treatments", "Targeted treatments for specific skin concerns");
        treatments.setImageUrl("https://cdn.create.vista.com/api/media/medium/182622870/stock-photo-skincare?token=");
        categoryRepository.save(treatments);
        
        // Create products
        createProduct("Gentle Foaming Cleanser", 
                     "A gentle, sulfate-free cleanser that removes dirt and makeup without stripping your skin of its natural oils. Perfect for all skin types, especially sensitive skin.",
                     new BigDecimal("24.99"), 50, "SKV-CLEAN-001", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Water, Glycerin, Sodium Lauryl Sulfoacetate, Cocamidopropyl Betaine, Sodium Cocoyl Isethionate, Chamomile Extract, Aloe Vera Extract",
                     "Apply to wet face, massage gently, and rinse with warm water. Use morning and evening.",
                     50.0, "15x5x5 cm", true);
        
        createProduct("Hydrating Daily Moisturizer", 
                     "A lightweight, non-greasy moisturizer that provides 24-hour hydration. Enriched with hyaluronic acid and ceramides for plump, healthy-looking skin.",
                     new BigDecimal("32.99"), 30, "SKV-MOIST-001", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Water, Hyaluronic Acid, Ceramides, Niacinamide, Glycerin, Dimethicone, Vitamin E",
                     "Apply to clean skin morning and evening. Gently massage until fully absorbed.",
                     60.0, "12x6x6 cm", true);
        
        createProduct("Vitamin C Brightening Serum", 
                     "A powerful antioxidant serum with 20% Vitamin C to brighten skin, reduce dark spots, and protect against environmental damage.",
                     new BigDecimal("45.99"), 25, "SKV-SERUM-001", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Ascorbic Acid, Water, Glycerin, Ferulic Acid, Vitamin E, Hyaluronic Acid",
                     "Apply 2-3 drops to clean skin in the morning. Follow with moisturizer and sunscreen.",
                     30.0, "10x3x3 cm", true);
        
        createProduct("Broad Spectrum SPF 50 Sunscreen", 
                     "A lightweight, non-greasy sunscreen that provides broad spectrum protection against UVA and UVB rays. Water-resistant for up to 80 minutes.",
                     new BigDecimal("28.99"), 40, "SKV-SUN-001", sunscreens, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Zinc Oxide, Octinoxate, Water, Glycerin, Dimethicone, Vitamin E",
                     "Apply liberally 15 minutes before sun exposure. Reapply every 2 hours or after swimming/sweating.",
                     45.0, "8x4x4 cm", false);
        
        createProduct("Detoxifying Clay Mask", 
                     "A purifying clay mask that draws out impurities and excess oil while soothing and calming the skin. Perfect for weekly deep cleansing.",
                     new BigDecimal("22.99"), 35, "SKV-MASK-001", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Kaolin Clay, Bentonite Clay, Water, Glycerin, Aloe Vera Extract, Tea Tree Oil",
                     "Apply a thin layer to clean skin, avoiding the eye area. Leave on for 10-15 minutes, then rinse with warm water.",
                     100.0, "12x8x8 cm", false);
        
        createProduct("Balancing Rose Toner", 
                     "A refreshing toner infused with rose water and witch hazel to balance pH levels and tighten pores. Alcohol-free formula.",
                     new BigDecimal("18.99"), 60, "SKV-TONER-001", toners, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Rose Water, Witch Hazel Extract, Water, Glycerin, Hyaluronic Acid, Vitamin E",
                     "Apply to clean skin with a cotton pad or by patting gently with hands. Use morning and evening.",
                     200.0, "15x6x6 cm", false);
        
        createProduct("Retinol Night Serum", 
                     "An advanced anti-aging serum with 1% retinol to reduce fine lines, improve skin texture, and promote cell turnover.",
                     new BigDecimal("55.99"), 20, "SKV-SERUM-002", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Retinol, Water, Glycerin, Hyaluronic Acid, Niacinamide, Vitamin E, Peptides",
                     "Apply 2-3 drops to clean skin in the evening. Start with every other night, then gradually increase to nightly use.",
                     30.0, "10x3x3 cm", true);
        
        createProduct("Gentle Exfoliating Scrub", 
                     "A gentle physical exfoliant with jojoba beads and natural enzymes to remove dead skin cells and reveal smoother, brighter skin.",
                     new BigDecimal("26.99"), 45, "SKV-CLEAN-002", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Water, Jojoba Beads, Glycerin, Papaya Extract, Aloe Vera Extract, Vitamin E",
                     "Use 2-3 times per week. Apply to wet skin, massage gently in circular motions, then rinse thoroughly.",
                     75.0, "12x6x6 cm", false);
        
        // Additional Cleansers
        createProduct("Oil Cleansing Balm", 
                     "A luxurious cleansing balm that melts away makeup and sunscreen while nourishing the skin with natural oils.",
                     new BigDecimal("34.99"), 40, "SKV-CLEAN-003", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Coconut Oil, Shea Butter, Jojoba Oil, Vitamin E, Essential Oils",
                     "Massage onto dry skin, add water to emulsify, then rinse thoroughly.",
                     80.0, "10x8x8 cm", false);
        
        createProduct("Salicylic Acid Cleanser", 
                     "A deep-cleansing face wash with 2% salicylic acid to unclog pores and prevent breakouts.",
                     new BigDecimal("28.99"), 35, "SKV-CLEAN-004", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Salicylic Acid, Water, Glycerin, Tea Tree Extract, Chamomile",
                     "Use daily for oily/acne-prone skin. Apply to wet face, massage, and rinse.",
                     60.0, "15x6x6 cm", false);
        
        // Additional Moisturizers
        createProduct("Night Repair Cream", 
                     "A rich, nourishing night cream with peptides and ceramides to repair and restore skin while you sleep.",
                     new BigDecimal("49.99"), 25, "SKV-MOIST-002", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Peptides, Ceramides, Hyaluronic Acid, Retinol, Vitamin E, Shea Butter",
                     "Apply to clean skin before bed. Gently massage until absorbed.",
                     50.0, "12x8x8 cm", true);
        
        createProduct("Gel Moisturizer for Oily Skin", 
                     "A lightweight, oil-free gel moisturizer that provides hydration without clogging pores.",
                     new BigDecimal("29.99"), 40, "SKV-MOIST-003", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Hyaluronic Acid, Niacinamide, Aloe Vera, Green Tea Extract, Vitamin C",
                     "Apply to clean skin morning and evening. Perfect for oily and combination skin.",
                     45.0, "10x6x6 cm", false);
        
        createProduct("Anti-Aging Day Cream", 
                     "A powerful anti-aging day cream with SPF 30 and advanced peptides to reduce fine lines and wrinkles.",
                     new BigDecimal("59.99"), 30, "SKV-MOIST-004", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Peptides, SPF 30, Hyaluronic Acid, Vitamin C, Retinol, Green Tea Extract",
                     "Apply to clean skin in the morning. Reapply sunscreen as needed.",
                     55.0, "12x8x8 cm", true);
        
        // Additional Serums
        createProduct("Hyaluronic Acid Serum", 
                     "A hydrating serum with 2% hyaluronic acid to plump and smooth skin, reducing the appearance of fine lines.",
                     new BigDecimal("39.99"), 50, "SKV-SERUM-003", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Hyaluronic Acid, Water, Glycerin, Vitamin E, Peptides",
                     "Apply 2-3 drops to clean skin morning and evening. Follow with moisturizer.",
                     30.0, "10x3x3 cm", true);
        
        createProduct("Niacinamide 10% Serum", 
                     "A powerful serum with 10% niacinamide to minimize pores, control oil production, and improve skin texture.",
                     new BigDecimal("32.99"), 45, "SKV-SERUM-004", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Niacinamide, Water, Glycerin, Hyaluronic Acid, Zinc",
                     "Apply 2-3 drops to clean skin morning and evening. Start with every other day.",
                     30.0, "10x3x3 cm", false);
        
        createProduct("Peptide Complex Serum", 
                     "An advanced anti-aging serum with multiple peptides to stimulate collagen production and improve skin firmness.",
                     new BigDecimal("65.99"), 20, "SKV-SERUM-005", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Peptides, Hyaluronic Acid, Vitamin C, Retinol, Vitamin E",
                     "Apply 2-3 drops to clean skin in the evening. Follow with moisturizer.",
                     30.0, "10x3x3 cm", true);
        
        // Additional Sunscreens
        createProduct("Mineral Sunscreen SPF 30", 
                     "A gentle, mineral-based sunscreen with zinc oxide that's safe for sensitive skin and reef-friendly.",
                     new BigDecimal("24.99"), 60, "SKV-SUN-002", sunscreens, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Zinc Oxide, Titanium Dioxide, Water, Glycerin, Vitamin E",
                     "Apply liberally 15 minutes before sun exposure. Reapply every 2 hours.",
                     50.0, "8x4x4 cm", false);
        
        createProduct("Tinted Sunscreen SPF 50", 
                     "A lightweight, tinted sunscreen that provides protection while evening out skin tone.",
                     new BigDecimal("36.99"), 35, "SKV-SUN-003", sunscreens, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Zinc Oxide, Octinoxate, Iron Oxides, Water, Glycerin",
                     "Apply evenly to face and neck. Can be worn alone or under makeup.",
                     40.0, "8x4x4 cm", true);
        
        // Additional Face Masks
        createProduct("Hydrating Sheet Mask", 
                     "A deeply hydrating sheet mask infused with hyaluronic acid and botanical extracts for instant moisture boost.",
                     new BigDecimal("8.99"), 100, "SKV-MASK-002", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Hyaluronic Acid, Aloe Vera, Green Tea Extract, Vitamin E, Glycerin",
                     "Apply to clean skin, leave on for 15-20 minutes, then remove and massage remaining serum.",
                     25.0, "20x15x1 cm", false);
        
        createProduct("Brightening Vitamin C Mask", 
                     "A potent vitamin C mask that brightens skin and reduces dark spots for a radiant complexion.",
                     new BigDecimal("29.99"), 30, "SKV-MASK-003", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Vitamin C, Niacinamide, Hyaluronic Acid, Vitamin E, Licorice Extract",
                     "Apply a thin layer to clean skin, leave on for 10-15 minutes, then rinse.",
                     80.0, "12x8x8 cm", true);
        
        createProduct("Exfoliating AHA Mask", 
                     "A gentle exfoliating mask with alpha hydroxy acids to remove dead skin cells and reveal smoother skin.",
                     new BigDecimal("34.99"), 25, "SKV-MASK-004", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Glycolic Acid, Lactic Acid, Hyaluronic Acid, Vitamin E, Aloe Vera",
                     "Use 1-2 times per week. Apply to clean skin, leave on for 5-10 minutes, then rinse.",
                     90.0, "12x8x8 cm", false);
        
        // Additional Toners
        createProduct("Hydrating Toner with Hyaluronic Acid", 
                     "A refreshing toner that provides instant hydration while balancing skin pH levels.",
                     new BigDecimal("22.99"), 55, "SKV-TONER-002", toners, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Hyaluronic Acid, Water, Glycerin, Aloe Vera, Vitamin E, Green Tea Extract",
                     "Apply to clean skin with a cotton pad or by patting gently with hands.",
                     200.0, "15x6x6 cm", true);
        
        createProduct("Exfoliating Toner with BHA", 
                     "A gentle exfoliating toner with beta hydroxy acid to unclog pores and improve skin texture.",
                     new BigDecimal("28.99"), 40, "SKV-TONER-003", toners, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Salicylic Acid, Water, Glycerin, Witch Hazel, Aloe Vera, Vitamin E",
                     "Use 2-3 times per week. Apply with a cotton pad, avoiding the eye area.",
                     150.0, "15x6x6 cm", false);
        
        // Eye Care Products
        createProduct("Anti-Aging Eye Cream", 
                     "A rich eye cream with peptides and caffeine to reduce puffiness, dark circles, and fine lines around the eyes.",
                     new BigDecimal("42.99"), 30, "SKV-EYE-001", eyeCare, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Peptides, Caffeine, Hyaluronic Acid, Vitamin C, Retinol, Vitamin K",
                     "Apply a small amount around the eye area morning and evening. Pat gently until absorbed.",
                     15.0, "6x3x3 cm", true);
        
        createProduct("Hydrating Eye Gel", 
                     "A lightweight eye gel that instantly hydrates and refreshes the delicate eye area.",
                     new BigDecimal("24.99"), 40, "SKV-EYE-002", eyeCare, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Hyaluronic Acid, Cucumber Extract, Aloe Vera, Vitamin E, Caffeine",
                     "Apply a small amount around the eye area morning and evening.",
                     15.0, "6x3x3 cm", false);
        
        // Specialized Treatments
        createProduct("Spot Treatment for Acne", 
                     "A targeted treatment with benzoyl peroxide to quickly clear acne and prevent future breakouts.",
                     new BigDecimal("18.99"), 60, "SKV-TREAT-001", treatments, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Benzoyl Peroxide, Salicylic Acid, Tea Tree Oil, Aloe Vera, Vitamin E",
                     "Apply a small amount directly to affected areas. Start with every other day.",
                     15.0, "8x3x3 cm", false);
        
        createProduct("Dark Spot Correcting Serum", 
                     "A powerful serum with vitamin C and niacinamide to fade dark spots and even skin tone.",
                     new BigDecimal("38.99"), 35, "SKV-TREAT-002", treatments, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Vitamin C, Niacinamide, Kojic Acid, Licorice Extract, Hyaluronic Acid",
                     "Apply 2-3 drops to clean skin morning and evening. Use sunscreen during the day.",
                     30.0, "10x3x3 cm", true);
    }
    
    private void initializeProducts() {
        System.out.println("Starting product initialization...");
        // Create categories
        Category cleansers = new Category("Cleansers", "Gentle cleansers for all skin types");
        cleansers.setImageUrl("https://oseamalibu.com/cdn/shop/files/OSEA_OceanCleanser_OC-1-DTC-1.jpg?v=1739317248&width=588");
        categoryRepository.save(cleansers);
        
        Category moisturizers = new Category("Moisturizers", "Hydrating moisturizers for healthy skin");
        moisturizers.setImageUrl("https://imgs.search.brave.com/azAWw8GjXOE7spO0w6JG0QQRpw70qsqMQLCqibhNous/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly84YzM0/MTJkNzYyMjVkMDRk/N2JhYS1iZTk4YjZl/YTE3OTIwOTUzZmI5/MzEyODJlZmY5YTY4/MS5pbWFnZXMubG92/ZWx5c2tpbi5jb20v/amx6enVoZXdfMjAy/NDA0MTYxMzA1NDU4/MzEwLmpwZw");
        categoryRepository.save(moisturizers);
        
        Category serums = new Category("Serums", "Concentrated treatments for targeted skin concerns");
        serums.setImageUrl("https://www.endro-cosmetiques.com/cdn/shop/files/serum-bonne-mine-10-de-vitamine-c-endro-cosmetiques-serums-visage-573577.webp?crop=center&height=952&v=1752731978&width=952");
        categoryRepository.save(serums);
        
        Category sunscreens = new Category("Sunscreens", "Protection from harmful UV rays");
        sunscreens.setImageUrl("https://m.media-amazon.com/images/I/410l-rym1yL._SX300_SY300_QL70_FMwebp_.jpg");
        categoryRepository.save(sunscreens);
        
        Category masks = new Category("Face Masks", "Weekly treatments for deep cleansing and nourishment");
        masks.setImageUrl("https://m.media-amazon.com/images/I/61An0jiJFvL._SX679_.jpg");
        categoryRepository.save(masks);
        
        Category toners = new Category("Toners", "Balancing and refreshing toners");
        toners.setImageUrl("https://pyxis.nymag.com/v1/imgs/129/9d7/7c474d34b1ea7f68d497d02b00b596d59a-toners-7-29.2x.rhorizontal.w700.jpg");
        categoryRepository.save(toners);
        
        Category eyeCare = new Category("Eye Care", "Specialized treatments for the delicate eye area");
        eyeCare.setImageUrl("https://imgs.search.brave.com/M1mtCnajUQ7P3IPaX-8hqh6t6oo2dS8jVbv5M8JedMs/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93d3cu/YnV0dGVyZmxpZXMt/ZXllY2FyZS5jby51/ay9jZG4vc2hvcC9m/aWxlcy9leWVjYXJl/LXByZXNlbnRhdGlv/bi1wYWNrLTMuanBn/P3Y9MTcyMjY4MDk4/MSZ3aWR0aD00ODA");
        categoryRepository.save(eyeCare);
        
        Category treatments = new Category("Treatments", "Targeted treatments for specific skin concerns");
        treatments.setImageUrl("https://cdn.create.vista.com/api/media/medium/182622870/stock-photo-skincare?token=");
        categoryRepository.save(treatments);
        
        // Create products
        createProduct("Gentle Foaming Cleanser", 
                     "A gentle, sulfate-free cleanser that removes dirt and makeup without stripping your skin of its natural oils. Perfect for all skin types, especially sensitive skin.",
                     new BigDecimal("24.99"), 50, "SKV-CLEAN-001", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Water, Glycerin, Sodium Lauryl Sulfoacetate, Cocamidopropyl Betaine, Sodium Cocoyl Isethionate, Chamomile Extract, Aloe Vera Extract",
                     "Apply to wet face, massage gently, and rinse with warm water. Use morning and evening.",
                     50.0, "15x5x5 cm", true);
        
        createProduct("Hydrating Daily Moisturizer", 
                     "A lightweight, non-greasy moisturizer that provides 24-hour hydration. Enriched with hyaluronic acid and ceramides for plump, healthy-looking skin.",
                     new BigDecimal("32.99"), 30, "SKV-MOIST-001", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Water, Hyaluronic Acid, Ceramides, Niacinamide, Glycerin, Dimethicone, Vitamin E",
                     "Apply to clean skin morning and evening. Gently massage until fully absorbed.",
                     60.0, "12x6x6 cm", true);
        
        createProduct("Vitamin C Brightening Serum", 
                     "A powerful antioxidant serum with 20% Vitamin C to brighten skin, reduce dark spots, and protect against environmental damage.",
                     new BigDecimal("45.99"), 25, "SKV-SERUM-001", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Ascorbic Acid, Water, Glycerin, Ferulic Acid, Vitamin E, Hyaluronic Acid",
                     "Apply 2-3 drops to clean skin in the morning. Follow with moisturizer and sunscreen.",
                     30.0, "10x3x3 cm", true);
        
        createProduct("Broad Spectrum SPF 50 Sunscreen", 
                     "A lightweight, non-greasy sunscreen that provides broad spectrum protection against UVA and UVB rays. Water-resistant for up to 80 minutes.",
                     new BigDecimal("28.99"), 40, "SKV-SUN-001", sunscreens, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Zinc Oxide, Octinoxate, Water, Glycerin, Dimethicone, Vitamin E",
                     "Apply liberally 15 minutes before sun exposure. Reapply every 2 hours or after swimming/sweating.",
                     45.0, "8x4x4 cm", false);
        
        createProduct("Detoxifying Clay Mask", 
                     "A purifying clay mask that draws out impurities and excess oil while soothing and calming the skin. Perfect for weekly deep cleansing.",
                     new BigDecimal("22.99"), 35, "SKV-MASK-001", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Kaolin Clay, Bentonite Clay, Water, Glycerin, Aloe Vera Extract, Tea Tree Oil",
                     "Apply a thin layer to clean skin, avoiding the eye area. Leave on for 10-15 minutes, then rinse with warm water.",
                     100.0, "12x8x8 cm", false);
        
        createProduct("Balancing Rose Toner", 
                     "A gentle, alcohol-free toner that balances pH levels and refreshes skin. Infused with rose water and hyaluronic acid for instant hydration.",
                     new BigDecimal("18.99"), 60, "SKV-TONER-001", toners, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Rose Water, Hyaluronic Acid, Glycerin, Aloe Vera Extract, Vitamin E",
                     "Apply to clean skin with a cotton pad or by patting gently with hands.",
                     200.0, "15x6x6 cm", true);
        
        createProduct("Retinol Night Serum", 
                     "A potent retinol serum that reduces fine lines, improves skin texture, and promotes cell turnover for younger-looking skin.",
                     new BigDecimal("55.99"), 20, "SKV-SERUM-002", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Retinol, Water, Glycerin, Hyaluronic Acid, Niacinamide, Vitamin E, Peptides",
                     "Apply 2-3 drops to clean skin in the evening. Start with every other night, then gradually increase to nightly use.",
                     30.0, "10x3x3 cm", true);
        
        createProduct("Gentle Exfoliating Scrub", 
                     "A gentle physical exfoliant with jojoba beads and natural enzymes to remove dead skin cells and reveal smoother, brighter skin.",
                     new BigDecimal("26.99"), 45, "SKV-CLEAN-002", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Water, Jojoba Beads, Glycerin, Papaya Extract, Aloe Vera Extract, Vitamin E",
                     "Use 2-3 times per week. Apply to wet skin, massage gently in circular motions, then rinse thoroughly.",
                     75.0, "12x6x6 cm", false);
        
        // Additional Cleansers
        createProduct("Oil Cleansing Balm", 
                     "A luxurious cleansing balm that melts away makeup and sunscreen while nourishing the skin with natural oils.",
                     new BigDecimal("34.99"), 40, "SKV-CLEAN-003", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Coconut Oil, Shea Butter, Jojoba Oil, Vitamin E, Essential Oils",
                     "Massage onto dry skin, add water to emulsify, then rinse thoroughly.",
                     80.0, "10x8x8 cm", false);
        
        createProduct("Salicylic Acid Cleanser", 
                     "A deep-cleansing face wash with 2% salicylic acid to unclog pores and prevent breakouts.",
                     new BigDecimal("28.99"), 35, "SKV-CLEAN-004", cleansers, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Salicylic Acid, Water, Glycerin, Tea Tree Extract, Chamomile",
                     "Use daily for oily/acne-prone skin. Apply to wet face, massage, and rinse.",
                     60.0, "15x6x6 cm", false);
        
        // Additional Moisturizers
        createProduct("Night Repair Cream", 
                     "A rich, nourishing night cream with peptides and ceramides to repair and restore skin while you sleep.",
                     new BigDecimal("49.99"), 25, "SKV-MOIST-002", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Peptides, Ceramides, Hyaluronic Acid, Retinol, Vitamin E, Shea Butter",
                     "Apply to clean skin before bed. Gently massage until absorbed.",
                     50.0, "12x8x8 cm", true);
        
        createProduct("Gel Moisturizer for Oily Skin", 
                     "A lightweight, oil-free gel moisturizer that provides hydration without clogging pores.",
                     new BigDecimal("29.99"), 40, "SKV-MOIST-003", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Hyaluronic Acid, Niacinamide, Aloe Vera, Green Tea Extract, Vitamin C",
                     "Apply to clean skin morning and evening. Perfect for oily and combination skin.",
                     45.0, "10x6x6 cm", false);
        
        createProduct("Anti-Aging Day Cream", 
                     "A powerful anti-aging day cream with SPF 30 and advanced peptides to reduce fine lines and wrinkles.",
                     new BigDecimal("59.99"), 30, "SKV-MOIST-004", moisturizers, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Peptides, SPF 30, Hyaluronic Acid, Vitamin C, Retinol, Green Tea Extract",
                     "Apply to clean skin in the morning. Reapply sunscreen as needed.",
                     55.0, "12x8x8 cm", true);
        
        // Additional Serums
        createProduct("Hyaluronic Acid Serum", 
                     "A hydrating serum with 2% hyaluronic acid to plump and smooth skin, reducing the appearance of fine lines.",
                     new BigDecimal("39.99"), 50, "SKV-SERUM-003", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Hyaluronic Acid, Water, Glycerin, Vitamin E, Peptides",
                     "Apply 2-3 drops to clean skin morning and evening. Follow with moisturizer.",
                     30.0, "10x3x3 cm", true);
        
        createProduct("Niacinamide 10% Serum", 
                     "A powerful serum with 10% niacinamide to minimize pores, control oil production, and improve skin texture.",
                     new BigDecimal("32.99"), 45, "SKV-SERUM-004", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Niacinamide, Water, Glycerin, Hyaluronic Acid, Zinc",
                     "Apply 2-3 drops to clean skin morning and evening. Start with every other day.",
                     30.0, "10x3x3 cm", false);
        
        createProduct("Peptide Complex Serum", 
                     "An advanced anti-aging serum with multiple peptides to stimulate collagen production and improve skin firmness.",
                     new BigDecimal("65.99"), 20, "SKV-SERUM-005", serums, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Peptides, Hyaluronic Acid, Vitamin C, Retinol, Vitamin E",
                     "Apply 2-3 drops to clean skin in the evening. Follow with moisturizer.",
                     30.0, "10x3x3 cm", true);
        
        // Additional Sunscreens
        createProduct("Mineral Sunscreen SPF 30", 
                     "A gentle, mineral-based sunscreen with zinc oxide that's safe for sensitive skin and reef-friendly.",
                     new BigDecimal("24.99"), 60, "SKV-SUN-002", sunscreens, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Zinc Oxide, Titanium Dioxide, Water, Glycerin, Vitamin E",
                     "Apply liberally 15 minutes before sun exposure. Reapply every 2 hours.",
                     50.0, "8x4x4 cm", false);
        
        createProduct("Tinted Sunscreen SPF 50", 
                     "A lightweight, tinted sunscreen that provides protection while evening out skin tone.",
                     new BigDecimal("36.99"), 35, "SKV-SUN-003", sunscreens, 
                     "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                     "Zinc Oxide, Octinoxate, Iron Oxides, Water, Glycerin",
                     "Apply evenly to face and neck. Can be worn alone or under makeup.",
                     40.0, "8x4x4 cm", true);
        
        // Additional Face Masks
        createProduct("Hydrating Sheet Mask", 
                     "A deeply hydrating sheet mask infused with hyaluronic acid and botanical extracts for instant moisture boost.",
                     new BigDecimal("8.99"), 100, "SKV-MASK-002", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Hyaluronic Acid, Aloe Vera, Green Tea Extract, Vitamin E, Glycerin",
                     "Apply to clean skin, leave on for 15-20 minutes, then remove and massage remaining serum.",
                     25.0, "20x15x1 cm", false);
        
        createProduct("Brightening Vitamin C Mask", 
                     "A potent vitamin C mask that brightens skin and reduces dark spots for a radiant complexion.",
                     new BigDecimal("29.99"), 30, "SKV-MASK-003", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Vitamin C, Niacinamide, Hyaluronic Acid, Vitamin E, Licorice Extract",
                     "Apply a thin layer to clean skin, leave on for 10-15 minutes, then rinse.",
                     80.0, "12x8x8 cm", true);
        
        createProduct("Exfoliating AHA Mask", 
                     "A gentle exfoliating mask with alpha hydroxy acids to remove dead skin cells and reveal smoother skin.",
                     new BigDecimal("34.99"), 25, "SKV-MASK-004", masks, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Glycolic Acid, Lactic Acid, Hyaluronic Acid, Vitamin E, Aloe Vera",
                     "Use 1-2 times per week. Apply to clean skin, leave on for 5-10 minutes, then rinse.",
                     90.0, "12x8x8 cm", false);
        
        // Additional Toners
        createProduct("Hydrating Toner with Hyaluronic Acid", 
                     "A refreshing toner that provides instant hydration while balancing skin pH levels.",
                     new BigDecimal("22.99"), 55, "SKV-TONER-002", toners, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Hyaluronic Acid, Water, Glycerin, Aloe Vera, Vitamin E, Green Tea Extract",
                     "Apply to clean skin with a cotton pad or by patting gently with hands.",
                     200.0, "15x6x6 cm", true);
        
        createProduct("Exfoliating Toner with BHA", 
                     "A gentle exfoliating toner with beta hydroxy acid to unclog pores and improve skin texture.",
                     new BigDecimal("28.99"), 40, "SKV-TONER-003", toners, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Salicylic Acid, Water, Glycerin, Witch Hazel, Aloe Vera, Vitamin E",
                     "Use 2-3 times per week. Apply with a cotton pad, avoiding the eye area.",
                     150.0, "15x6x6 cm", false);
        
        // Eye Care Products
        createProduct("Anti-Aging Eye Cream", 
                     "A rich eye cream with peptides and caffeine to reduce puffiness, dark circles, and fine lines around the eyes.",
                     new BigDecimal("42.99"), 30, "SKV-EYE-001", eyeCare, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Peptides, Caffeine, Hyaluronic Acid, Vitamin C, Retinol, Vitamin K",
                     "Apply a small amount around the eye area morning and evening. Pat gently until absorbed.",
                     15.0, "6x3x3 cm", true);
        
        createProduct("Hydrating Eye Gel", 
                     "A lightweight eye gel that instantly hydrates and refreshes the delicate eye area.",
                     new BigDecimal("24.99"), 40, "SKV-EYE-002", eyeCare, 
                     "https://images.unsplash.com/photo-1570194065650-d99fb4bedf0a?w=400",
                     "Hyaluronic Acid, Cucumber Extract, Aloe Vera, Vitamin E, Caffeine",
                     "Apply a small amount around the eye area morning and evening.",
                     15.0, "6x3x3 cm", false);
        
        // Specialized Treatments
        createProduct("Spot Treatment for Acne", 
                     "A targeted treatment with benzoyl peroxide to quickly clear acne and prevent future breakouts.",
                     new BigDecimal("18.99"), 60, "SKV-TREAT-001", treatments, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Benzoyl Peroxide, Salicylic Acid, Tea Tree Oil, Aloe Vera, Vitamin E",
                     "Apply a small amount directly to affected areas. Start with every other day.",
                     15.0, "8x3x3 cm", false);
        
        createProduct("Dark Spot Correcting Serum", 
                     "A powerful serum with vitamin C and niacinamide to fade dark spots and even skin tone.",
                     new BigDecimal("38.99"), 35, "SKV-TREAT-002", treatments, 
                     "https://images.unsplash.com/photo-1599305445771-7760da85a3c1?w=400",
                     "Vitamin C, Niacinamide, Kojic Acid, Licorice Extract, Hyaluronic Acid",
                     "Apply 2-3 drops to clean skin morning and evening. Use sunscreen during the day.",
                     30.0, "10x3x3 cm", true);
        
        System.out.println("Product initialization completed. Total products: " + productRepository.count());
    }
    
    private void createProduct(String name, String description, BigDecimal price, Integer stock, 
                             String sku, Category category, String imageUrl, String ingredients, 
                             String usageInstructions, Double weight, String dimensions, Boolean isFeatured) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setSku(sku);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setIngredients(ingredients);
        product.setUsageInstructions(usageInstructions);
        product.setWeight(weight);
        product.setDimensions(dimensions);
        product.setIsFeatured(isFeatured);
        product.setIsActive(true);
        productRepository.save(product);
    }
}
