package com.example.action.dto;

import com.example.action.model.Status;
import lombok.Data;

@Data
public class Order {

    private Long id;

    private Long passengerId;

    private Long driverId;

    private Status status;

    private String startPosition;

    private String finishPosition;

    private Double distance;

    private String bill;

    private Double rating;
}
