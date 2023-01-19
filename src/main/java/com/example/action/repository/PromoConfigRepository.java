package com.example.action.repository;

import com.example.action.dto.CacheDTO;
import com.example.action.dto.PromoConfigDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = "promo")
public class PromoConfigRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_PROMO_CONFIG = "security.promo_config";
    private final static String FIELD_PROMO_TYPE = "promo_type";
    private final static String FIELD_DISCOUNT = "discount";
    private final static String FIELD_TRIPS_COUNT = "trips_count";

    public List<PromoConfigDTO> getPromoConfigs(String key) {
        return getPromoConfig(key).getPromoConfigs();
    }

    @CacheEvict(allEntries = true)
    @Scheduled(initialDelay = 1000L, fixedDelay = 120000L)
    public void clearCache() {}

    @Cacheable
    public CacheDTO getPromoConfig(String key) {
        List<PromoConfigDTO> value = dslContext
                .selectFrom(DSL.table(TABLE_NAME_PROMO_CONFIG))
                .fetchInto(PromoConfigDTO.class);

        return new CacheDTO(key, value);
    }

    public void createPromoConfig(PromoConfigDTO promoTypeDTO) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_PROMO_CONFIG))
                .columns(DSL.field(FIELD_PROMO_TYPE), DSL.field(FIELD_DISCOUNT), DSL.field(FIELD_TRIPS_COUNT))
                .values(promoTypeDTO.getPromoType(), promoTypeDTO.getDiscount(), promoTypeDTO.getTripsCount())
                .execute();
    }
}