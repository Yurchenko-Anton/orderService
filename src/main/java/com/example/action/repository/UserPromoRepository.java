package com.example.action.repository;

import com.example.action.dto.PromoConfigDTO;
import com.example.action.dto.UserPromoDTO;
import com.example.action.model.PromoStatus;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPromoRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_USERS_PROMO = "security.users_promo";
    private final static String FIELD_ID = "id";
    private final static String FIELD_USER_ID = "user_id";
    private final static String FIELD_PROMO_CONFIG_ID = "promo_config_id";
    private final static String FIELD_DISCOUNT = "discount";
    private final static String FIELD_IS_ACTIVE = "is_active";

    public List<UserPromoDTO> getAllUserPromo(Long userId) {
        return dslContext
                .select(DSL.field(FIELD_ID), DSL.field(FIELD_DISCOUNT))
                .from(TABLE_NAME_USERS_PROMO)
                .where(DSL.field(FIELD_USER_ID).eq(userId))
                .fetchInto(UserPromoDTO.class);
    }

    public void addPromoToUser(Long userId, PromoConfigDTO promoTypeDTO) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_USERS_PROMO))
                .columns(DSL.field(FIELD_USER_ID), DSL.field(FIELD_PROMO_CONFIG_ID), DSL.field(FIELD_DISCOUNT), DSL.field(FIELD_IS_ACTIVE))
                .values(userId, promoTypeDTO.getId(), promoTypeDTO.getDiscount(), PromoStatus.INACTIVE.name())
                .execute();
    }

    public boolean checkIfNotExist(Long userId, PromoConfigDTO promoTypeDTO) {
        return dslContext
                .select(DSL.field(FIELD_ID), DSL.field(FIELD_DISCOUNT))
                .from(DSL.table(TABLE_NAME_USERS_PROMO))
                .where(DSL.field(FIELD_USER_ID).eq(userId))
                .and(DSL.field(FIELD_PROMO_CONFIG_ID).eq(promoTypeDTO.getId()))
                .fetchInto(UserPromoDTO.class).isEmpty();
    }

    public void activationPromo(Long userId, int promoId) {
        dslContext
                .update(DSL.table(TABLE_NAME_USERS_PROMO))
                .set(DSL.field(FIELD_IS_ACTIVE), PromoStatus.ACTIVE.name())
                .where(DSL.field(FIELD_ID).eq(promoId))
                .and(DSL.field(FIELD_USER_ID).eq(userId))
                .execute();
    }

    public List<UserPromoDTO> getActivatedUserPromo(Long userId) {
        return dslContext
                .select(DSL.field(FIELD_ID), DSL.field(FIELD_DISCOUNT))
                .from(DSL.table(TABLE_NAME_USERS_PROMO))
                .where(DSL.field(FIELD_USER_ID).eq(userId))
                .and(DSL.field(FIELD_IS_ACTIVE).eq(PromoStatus.ACTIVE.name()))
                .fetchInto(UserPromoDTO.class);
    }
}