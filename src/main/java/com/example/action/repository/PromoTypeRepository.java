package com.example.action.repository;

import com.example.action.dto.PromoTypeDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromoTypeRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_PROMO = "security.promo_types";
    private final static String FIELD_ID = "id";
    private final static String FIELD_PROMO_TYPE = "promo_type";
    private final static String FIELD_DISCOUNT = "discount";
    private final static String FIELD_TRIPS_COUNT = "trips_count";

    public List<PromoTypeDTO> getPromoTypes(){
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_PROMO))
                .fetchInto(PromoTypeDTO.class);
    }

    public void createPromoType(PromoTypeDTO promoTypeDTO){
        dslContext
                .insertInto(DSL.table(TABLE_NAME_PROMO))
                .columns(DSL.field(FIELD_PROMO_TYPE), DSL.field(FIELD_DISCOUNT), DSL.field(FIELD_TRIPS_COUNT))
                .values(promoTypeDTO.getPromoType(), promoTypeDTO.getDiscount(), promoTypeDTO.getTripsCount())
                .execute();
    }
}
