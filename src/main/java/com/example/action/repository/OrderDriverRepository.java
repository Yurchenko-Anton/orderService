package com.example.action.repository;

import com.example.action.dto.Order;
import com.example.action.model.Status;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.jooq.impl.DSL.avg;

@Service
@RequiredArgsConstructor
public class OrderDriverRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_FINISH_ORDER = "security.finish_order";
    private final static String TABLE_NAME_ORDER = "security.order";
    private final static String FIELD_ID = "id";
    private final static String FIELD_PASSENGER_ID = "passenger_id";
    private final static String FIELD_DRIVER_ID = "driver_id";
    private final static String FIELD_STATUS = "status";
    private final static String FIELD_DISTANCE = "distance";
    private final static String FIELD_BILL = "bill";
    private final static String FIELD_RATING = "rating";

    public List<Order> emptyOrders() {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_DRIVER_ID).isNull()).fetchInto(Order.class);
    }

    public ResponseEntity<Order> acceptOrder(Long orderId, Long driverId) {
        dslContext
                .update(DSL.table(TABLE_NAME_ORDER))
                .set(DSL.field(FIELD_DRIVER_ID), driverId)
                .set(DSL.field(FIELD_STATUS), Status.PERFORMING.name())
                .where(DSL.field(FIELD_ID).equal(orderId))
                .execute();

        return findOrder(driverId);
    }

    public ResponseEntity<Double> avgRating(Long id) {
        return ResponseEntity.ok((double) dslContext
                .select(avg(DSL.field(FIELD_RATING).cast(Double.class)))
                .from(DSL.table(TABLE_NAME_FINISH_ORDER))
                .where(DSL.field(FIELD_DRIVER_ID).equal(id))
                .execute());
    }

    public ResponseEntity<Order> finishDriverOrder(Long id) {
        dslContext
                .update(DSL.table(TABLE_NAME_ORDER))
                .set(DSL.field(FIELD_STATUS), Status.ARRIVED.name())
                .where(DSL.field(FIELD_DRIVER_ID).equal(id))
                .execute();
        return findOrder(id);
    }

    private ResponseEntity<Order> findOrder(Long id) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .fetchInto(Order.class).stream().findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
