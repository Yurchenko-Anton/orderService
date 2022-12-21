package com.example.action.repository;

import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.model.Status;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPassengerRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_FINISH_ORDER = "security.finish_order";
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

    public List<Order> userOrders(Long id, Pageable pageable) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_FINISH_ORDER))
                .where(DSL.field(FIELD_PASSENGER_ID).equal(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(Order.class);
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
        Order order = findOrder(orderId);
        deleteFinishedOrder(order);
        addToHistory(order);
        return order;
    }

    private Order findOrder(Long id) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_ID).equal(id))
                .fetchInto(Order.class)
                .stream()
                .findFirst().orElseThrow();
    }

    private void deleteFinishedOrder(Order order) {
        dslContext
                .deleteFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_ID).equal(order.getId()))
                .execute();
    }

    private void addToHistory(Order order) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_FINISH_ORDER))
                .columns(DSL.field(FIELD_PASSENGER_ID),
                        DSL.field(FIELD_DRIVER_ID),
                        DSL.field(FIELD_STATUS),
                        DSL.field(FIELD_START_POSITION),
                        DSL.field(FIELD_FINISH_POSITION),
                        DSL.field(FIELD_DISTANCE),
                        DSL.field(FIELD_BILL),
                        DSL.field(FIELD_RATING))
                .values(order.getPassengerId(),
                        order.getDriverId(),
                        order.getStatus().name(),
                        order.getStartPosition(),
                        order.getFinishPosition(),
                        order.getDistance(),
                        order.getBill(),
                        order.getRating())
                .execute();
    }
}