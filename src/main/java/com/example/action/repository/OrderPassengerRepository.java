package com.example.action.repository;

import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.model.Status;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPassengerRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_ORDER = "security.order";
    private final static String FIELD_ID = "id";
    private final static String FIELD_PASSENGER_ID = "passenger_id";
    private final static String FIELD_DRIVER_ID = "driver_id";
    private final static String FIELD_STATUS = "status";
    private final static String FIELD_START_POSITION = "start_position";
    private final static String FIELD_FINISH_POSITION = "finish_position";
    private final static String FIELD_DISTANCE = "distance";
    private final static String FIELD_BILL = "bill";
    private final static String FIELD_RATING = "rating";

    public ResponseEntity<CreateOrderDTO> createOrder(Long id, CreateOrderDTO createOrderDTO, Double distance) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_ORDER))
                .columns(DSL.field(FIELD_PASSENGER_ID), DSL.field(FIELD_STATUS), DSL.field(FIELD_DISTANCE), DSL.field(FIELD_START_POSITION), DSL.field(FIELD_FINISH_POSITION))
                .values(id, Status.WAITING.name(), distance, createOrderDTO.getStartPosition(), createOrderDTO.getFinishPosition())
                .execute();
        return ResponseEntity.ok(createOrderDTO);
    }

    public List<Order> activeOrder(Long id) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_PASSENGER_ID).equal(id))
                .fetchInto(Order.class);
    }

    public Order finishOrder(Long id, Long orderId, int rating) {
        dslContext
                .update(DSL.table(TABLE_NAME_ORDER))
                .set(DSL.field(FIELD_RATING), rating)
                .set(DSL.field(FIELD_STATUS), Status.FINISHED.name())
                .where(DSL.field(FIELD_ID).equal(orderId).and(DSL.field(FIELD_STATUS).equal(Status.ARRIVED.name()))
                        .and(DSL.field(FIELD_PASSENGER_ID).equal(id)))
                .execute();
        return findOrder(orderId);
    }

    private Order findOrder(Long id) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_ID).equal(id))
                .fetchInto(Order.class)
                .stream()
                .findFirst().orElseThrow();
    }

    public void deleteFinishedOrder(Order order) {
        dslContext
                .deleteFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_ID).equal(order.getId()))
                .execute();
    }
}