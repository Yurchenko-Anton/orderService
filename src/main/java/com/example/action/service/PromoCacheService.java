package com.example.action.service;

import com.example.action.dto.PromoTypeDTO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PromoCacheService {

    private static final String KEY = "promoConfig";

    private final Cache<String, List<PromoTypeDTO>> promoCache = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();

    public List<PromoTypeDTO> getCacheValues() {
        return promoCache.getIfPresent(KEY);
    }

    public void addValuesToCache(List<PromoTypeDTO> value) {
        if (value != null) {
            promoCache.put(KEY, value);
        }
    }
}