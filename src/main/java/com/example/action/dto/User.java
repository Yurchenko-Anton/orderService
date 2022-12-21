package com.example.action.dto;

import com.example.action.model.Role;
import lombok.Data;

@Data
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private Role role;
}