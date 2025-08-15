package com.project.service;

import com.project.dto.CustomerDTO;
import com.project.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerDTO getCustomerById(Long id);

    void updateCustomer(Long id, Customer updatedCustomer);

    void deleteCustomer(Long id);

    List<CustomerDTO> getCustomersByAddress(String address);

    List<CustomerDTO> getAllCustomers();

    List<CustomerDTO> getCustomersByName(String name);

    List<CustomerDTO> getCustomersByEmail(String email);

    List<CustomerDTO> getCustomersByPhoneNumber(String phoneNumber);
}
