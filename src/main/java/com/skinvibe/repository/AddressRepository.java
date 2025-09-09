package com.skinvibe.repository;

import com.skinvibe.model.Address;
import com.skinvibe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByUser(User user);
    
    List<Address> findByUserAndAddressType(User user, Address.AddressType addressType);
    
    Optional<Address> findByUserAndIsDefaultTrue(User user);
    
    Optional<Address> findByUserAndIsDefaultTrueAndAddressType(User user, Address.AddressType addressType);
}
