package com.example.action.dto;

import com.example.action.model.Status;
import lombok.Data;

@Data
public class Order {

    Long id;

    Long passengerId;

    Long driverId;

    Status status;

    String startPosition;

    String finishPosition;

    Double distance;

    String bill;

    Double rating;
}