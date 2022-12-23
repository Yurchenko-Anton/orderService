package com.example.action.repository;

import com.example.action.dto.Order;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.jooq.impl.DSL.avg;

@Component
@RequiredArgsConstructor
public class OrderHistoryRepository {

    private final DSLContext dslContext;

    private final static String TABLE_NAME_ORDER_HISTORY = "security.order_history";
    private final static String FIELD_ID = "id";
    private final static String FIELD_PASSENGER_ID = "passenger_id";
    private final static String FIELD_DRIVER_ID = "driver_id";
    private final static String FIELD_STATUS = "status";
    private final static String FIELD_START_POSITION = "start_position";
    private final static String FIELD_FINISH_POSITION = "finish_position";
    private final static String FIELD_DISTANCE = "distance";
    private final static String FIELD_BILL = "bill";
    private final static String FIELD_RATING = "rating";

    public List<Order> userOrders(Long id, Pageable pageable) {
        return dslContext
                .selectFrom(DSL.table(TABLE_NAME_ORDER_HISTORY))
                .where(DSL.field(FIELD_PASSENGER_ID).equal(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(Order.class);
    }

    public ResponseEntity<Double> avgRating(Long id) {
        return ResponseEntity.ok((double) dslContext
                .select(avg(DSL.field(FIELD_RATING).cast(Double.class)))
                .from(DSL.table(TABLE_NAME_ORDER_HISTORY))
                .where(DSL.field(FIELD_DRIVER_ID).equal(id))
                .execute());
    }

    public void addToHistory(Order order) {
        dslContext
                .insertInto(DSL.table(TABLE_NAME_ORDER_HISTORY))
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
