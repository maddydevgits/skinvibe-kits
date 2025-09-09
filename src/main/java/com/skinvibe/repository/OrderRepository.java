package com.skinvibe.repository;

import com.skinvibe.model.Order;
import com.skinvibe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    Page<Order> findByUser(User user, Pageable pageable);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByOrderStatus(Order.OrderStatus orderStatus);
    
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    long countByOrderStatus(Order.OrderStatus orderStatus);
}
