package com.example.action.repository;

import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.model.Status;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAdminRepository {

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

    public ResponseEntity<CreateOrderDTO> createOrder(CreateOrderDTO createOrderDTO, Double distance) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_ORDER))
                .columns(DSL.field(FIELD_PASSENGER_ID), DSL.field(FIELD_STATUS), DSL.field(FIELD_DISTANCE), DSL.field(FIELD_START_POSITION), DSL.field(FIELD_FINISH_POSITION))
                .values(createOrderDTO.getGuestId(), Status.WAITING.name(), distance, createOrderDTO.getStartPosition(), createOrderDTO.getFinishPosition())
                .execute();
        return ResponseEntity.ok(createOrderDTO);
    }

    public List<Order> activeOrder() {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .fetchInto(Order.class);
    }

    public ResponseEntity<Order> finishDriverOrder(Long id) {
        dslContext
                .update(DSL.table(TABLE_NAME_ORDER))
                .set(DSL.field(FIELD_STATUS), Status.ARRIVED.name())
                .where(DSL.field(FIELD_ID).equal(id))
                .execute();
        return findOrder(id);
    }

    public ResponseEntity<Order> changeRoute(Long id, CreateOrderDTO createOrderDTO) {
        dslContext
                .update(DSL.table(TABLE_NAME_ORDER))
                .set(DSL.field(FIELD_START_POSITION), createOrderDTO.getStartPosition())
                .set(DSL.field(FIELD_FINISH_POSITION), createOrderDTO.getFinishPosition())
                .where(DSL.field(FIELD_ID).equal(id))
                .execute();
        return findOrder(id);
    }

    public List<Order> userHistory(Long id, Pageable pageable) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_FINISH_ORDER))
                .where(DSL.field(FIELD_PASSENGER_ID).equal(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(Order.class);
    }

    public ResponseEntity<Order> rejectDriver(Long id) {
        dslContext
                .update(DSL.table(TABLE_NAME_ORDER))
                .set(DSL.field(FIELD_DRIVER_ID), (Long) null)
                .set(DSL.field(FIELD_STATUS), Status.WAITING.name())
                .where(DSL.field(FIELD_ID).equal(id))
                .execute();
        return findOrder(id);
    }

    private ResponseEntity<Order> findOrder(Long id) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER))
                .where(DSL.field(FIELD_ID).equal(id))
                .fetchInto(Order.class).stream().findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
