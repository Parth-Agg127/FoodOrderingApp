package com.project.service.impl;

import com.project.dto.CustomerDTO;
import com.project.dto.map.CustomerMapper;
import com.project.entity.Customer;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CustomerRepository;
import com.project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return customerMapper.toDto(customer);
    }

    @Override
    public void updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerDTO> getCustomersByAddress(String address) {
        List<Customer> customers = customerRepository.findByAddress(address);
        return customerMapper.toDtoList(customers);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDtoList(customers);
    }

    @Override
    public List<CustomerDTO> getCustomersByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customerMapper.toDtoList(customers);
    }

    @Override
    public List<CustomerDTO> getCustomersByEmail(String email) {
        List<Customer> customers = customerRepository.findAllByEmail(email);
        return customerMapper.toDtoList(customers);
    }

    @Override
    public List<CustomerDTO> getCustomersByPhoneNumber(String phoneNumber) {
        List<Customer> customers = customerRepository.findByPhoneNumber(phoneNumber);
        return customerMapper.toDtoList(customers);
    }
}
