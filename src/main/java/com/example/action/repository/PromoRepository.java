package com.example.action.repository;

import com.example.action.dto.Promo;
import com.example.action.model.PromoStatus;
import com.example.action.model.PromoType;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromoRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_PROMO = "security.promo";
    private final static String FIELD_ID = "id";
    private final static String FIELD_USER_ID = "user_id";
    private final static String FIELD_PROMO_NAME = "promo_name";
    private final static String FIELD_DISCOUNT = "discount";
    private final static String FIELD_IS_ACTIVE = "is_active";

    public List<Promo> getPromos(Long userId) {
        return dslContext
                .select(DSL.field(FIELD_ID), DSL.field(FIELD_DISCOUNT))
                .from(TABLE_NAME_PROMO)
                .where(DSL.field(FIELD_USER_ID).eq(userId))
                .fetchInto(Promo.class);
    }

    public void addPromo(Long userId, PromoType promoType) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_PROMO))
                .columns(DSL.field(FIELD_USER_ID), DSL.field(FIELD_PROMO_NAME), DSL.field(FIELD_DISCOUNT), DSL.field(FIELD_IS_ACTIVE))
                .values(userId, promoType.name(), promoType.getDiscount(), PromoStatus.INACTIVE.name())
                .execute();
    }

    public boolean checkIfNotExist(Long userId, PromoType promoType) {
        return dslContext
                .select(DSL.field(FIELD_ID), DSL.field(FIELD_DISCOUNT))
                .from(DSL.table(TABLE_NAME_PROMO))
                .where(DSL.field(FIELD_USER_ID).eq(userId))
                .and(DSL.field(FIELD_PROMO_NAME).eq(promoType.name()))
                .fetchInto(Promo.class).isEmpty();
    }

    public void activationPromo(Long userId, int promoId) {
        dslContext
                .update(DSL.table(TABLE_NAME_PROMO))
                .set(DSL.field(FIELD_IS_ACTIVE), PromoStatus.ACTIVE.name())
                .where(DSL.field(FIELD_ID).eq(promoId))
                .and(DSL.field(FIELD_USER_ID).eq(userId))
                .execute();
    }

    public List<Promo> getActivatedUserPromo(Long userId) {
        return dslContext
                .select(DSL.field(FIELD_ID), DSL.field(FIELD_DISCOUNT))
                .from(DSL.table(TABLE_NAME_PROMO))
                .where(DSL.field(FIELD_USER_ID).eq(userId))
                .and(DSL.field(FIELD_IS_ACTIVE).eq(PromoStatus.ACTIVE.name()))
                .fetchInto(Promo.class);
    }
}