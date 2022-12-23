package com.example.action.dto;

import com.example.action.model.Role;
import lombok.Value;

@Value
public class User {

    Long id;

    String firstName;

    String lastName;

    String phone;

    Role role;
}