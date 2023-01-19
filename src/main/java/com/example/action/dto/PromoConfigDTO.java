package com.example.action.dto;

import lombok.Value;

@Value
public class PromoConfigDTO {
    int id;
    String promoType;
    int discount;
    int tripsCount;
}
