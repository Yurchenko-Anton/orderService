package com.example.action.service;

import com.example.action.client.FeignDistanceClient;
import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.repository.OrderAdminRepository;
import com.example.action.repository.OrderHistoryRepository;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
@AllArgsConstructor
public class AdminsServiceTest {

    @Mock
    private OrderAdminRepository orderAdminRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private FeignDistanceClient feignDistanceClient;

    @InjectMocks
    private AdminsService adminsService;

    @Test
    public void shouldCreateOrder() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO("startPosition", "finishPosition", null);

        when(feignDistanceClient.getDistance(createOrderDTO)).thenReturn(100.0);
        when(orderAdminRepository.createOrder(eq(createOrderDTO), anyDouble())).thenReturn(ResponseEntity.ok(createOrderDTO));

        assertThat(adminsService.createOrder(createOrderDTO)).isEqualTo(ResponseEntity.ok(createOrderDTO));
        verify(orderAdminRepository, times(1)).createOrder(Matchers.any(CreateOrderDTO.class), anyDouble());
    }

    @Test
    public void shouldGetActiveOrders() {
        List<Order> orders = new ArrayList<>();

        when(orderAdminRepository.activeOrder()).thenReturn(orders);

        assertThat(adminsService.activeOrders()).isEmpty();
        verify(orderAdminRepository, times(1)).activeOrder();
    }

    @Test
    public void shouldFinishOrder() {
        Order order = new Order();

        when(orderAdminRepository.finishDriverOrder(anyLong())).thenReturn(ResponseEntity.ok(order));

        assertThat(adminsService.finishOrder(anyLong())).isEqualTo(ResponseEntity.ok(order));
        verify(orderAdminRepository, times(1)).finishDriverOrder(anyLong());
    }

    @Test
    public void shouldChangeRoute() {
        Order order = new Order();
        CreateOrderDTO createOrderDTO = new CreateOrderDTO("startPosition", "newFinishPosition", null);

        when(orderAdminRepository.changeRoute(anyLong(), any(CreateOrderDTO.class))).thenReturn(ResponseEntity.ok(order));

        assertThat(adminsService.changeRoute(anyLong(), eq(createOrderDTO))).isEqualTo(ResponseEntity.ok(order));
        verify(orderAdminRepository, times(1)).changeRoute(anyLong(), Matchers.any(CreateOrderDTO.class));
    }

    @Test
    public void shouldGetUserHistory() {
        List<Order> orders = new ArrayList<>();

        when(orderHistoryRepository.userOrders(anyLong(), any(Pageable.class))).thenReturn(orders);

        assertThat(adminsService.userHistory(anyLong(), any(Pageable.class))).isEmpty();
        verify(orderHistoryRepository, times(1)).userOrders(anyLong(), any(Pageable.class));
    }

    @Test
    public void shouldRejectDriver() {
        Order order = new Order();

        when(orderAdminRepository.rejectDriver(anyLong())).thenReturn(ResponseEntity.ok(order));

        assertThat(adminsService.rejectDriver(anyLong())).isEqualTo(ResponseEntity.ok(order));
        verify(orderAdminRepository, times(1)).rejectDriver(anyLong());
    }
}
