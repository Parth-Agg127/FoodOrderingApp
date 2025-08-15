package com.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO(String token, String loginSuccessful) {
        this.token = token;
        System.out.println(loginSuccessful);
    }

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
