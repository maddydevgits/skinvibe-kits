package com.skinvibe.service;

import com.skinvibe.model.Address;
import com.skinvibe.model.User;
import com.skinvibe.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressService {
    
    @Autowired
    private AddressRepository addressRepository;
    
    public List<Address> getAddressesByUser(User user) {
        return addressRepository.findByUser(user);
    }
    
    public List<Address> getAddressesByUserAndType(User user, Address.AddressType addressType) {
        return addressRepository.findByUserAndAddressType(user, addressType);
    }
    
    public Optional<Address> getDefaultAddress(User user) {
        return addressRepository.findByUserAndIsDefaultTrue(user);
    }
    
    public Optional<Address> getDefaultAddressByType(User user, Address.AddressType addressType) {
        return addressRepository.findByUserAndIsDefaultTrueAndAddressType(user, addressType);
    }
    
    public Address saveAddress(Address address) {
        // If this is set as default, unset other default addresses of the same type
        if (address.getIsDefault()) {
            setAsDefaultAddress(address);
        }
        return addressRepository.save(address);
    }
    
    public Address updateAddress(Address address) {
        // If this is set as default, unset other default addresses of the same type
        if (address.getIsDefault()) {
            setAsDefaultAddress(address);
        }
        return addressRepository.save(address);
    }
    
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
    
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }
    
    private void setAsDefaultAddress(Address newDefaultAddress) {
        // Find existing default address of the same type for the same user
        Optional<Address> existingDefault = addressRepository
                .findByUserAndIsDefaultTrueAndAddressType(
                        newDefaultAddress.getUser(), 
                        newDefaultAddress.getAddressType()
                );
        
        // Unset the existing default address
        if (existingDefault.isPresent() && !existingDefault.get().getId().equals(newDefaultAddress.getId())) {
            Address existing = existingDefault.get();
            existing.setIsDefault(false);
            addressRepository.save(existing);
        }
    }
}
