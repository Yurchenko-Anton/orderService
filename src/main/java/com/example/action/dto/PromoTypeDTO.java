package com.example.action.dto;

import lombok.Value;

@Value
public class PromoTypeDTO {
    int id;
    String promoType;
    int discount;
    int tripsCount;
}
