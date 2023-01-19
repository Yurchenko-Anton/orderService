package com.example.action.dto;

import lombok.Value;

import java.util.List;

@Value
public class CacheDTO {
    String key;
    List<PromoConfigDTO> promoConfigs;
}