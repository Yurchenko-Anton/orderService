package com.example.action.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CreateOrderDTO {

    @NotBlank
    String startPosition;

    @NotBlank
    String finishPosition;

    Long guestId;
}
