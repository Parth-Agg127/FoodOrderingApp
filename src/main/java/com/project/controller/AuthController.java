package com.project.controller;

import com.project.config.JwtUtil;
import com.project.dto.LoginRequestDTO;
import com.project.dto.LoginResponseDTO;
import com.project.entity.Customer;
import com.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new customer
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        // Encode password before saving
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepo.save(customer);
        return ResponseEntity.ok("Customer registered successfully");
    }

    // Login existing customer
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // Authenticate user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generate JWT token
        String token = jwtUtil.generateToken(request.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
