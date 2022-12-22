package com.example.action.service;

import com.example.action.client.FeignDistanceClient;
import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.repository.OrderAdminRepository;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@AllArgsConstructor
public class AdminsServiceTest {

    @Mock
    private OrderAdminRepository orderAdminRepository;

    @Mock
    private FeignDistanceClient feignDistanceClient;

    @InjectMocks
    private AdminsService adminsService;

    @Test
    public void createOrder() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO("F", "S", null);

        when(feignDistanceClient.getDistance(Matchers.any(CreateOrderDTO.class))).thenReturn(100.0);
        when(orderAdminRepository.createOrder(any(CreateOrderDTO.class), anyDouble())).thenReturn(ResponseEntity.ok(createOrderDTO));

        Assertions.assertThat(adminsService.createOrder(createOrderDTO)).isEqualTo(ResponseEntity.ok(createOrderDTO));
        Mockito.verify(orderAdminRepository, times(1)).createOrder(Matchers.any(CreateOrderDTO.class), anyDouble());
    }

    @Test
    public void activeOrders() {
        List<Order> orderList = new ArrayList<>();

        when(orderAdminRepository.activeOrder()).thenReturn(orderList);

        Assertions.assertThat(adminsService.activeOrders()).isEmpty();
        Mockito.verify(orderAdminRepository, times(1)).activeOrder();
    }

    @Test
    public void finishOrder() {
        Order order = new Order();

        when(orderAdminRepository.finishDriverOrder(anyLong())).thenReturn(ResponseEntity.ok(order));

        Assertions.assertThat(adminsService.finishOrder(anyLong())).isEqualTo(ResponseEntity.ok(order));
        Mockito.verify(orderAdminRepository, times(1)).finishDriverOrder(anyLong());
    }

    @Test
    public void changeRoute() {
        Order order = new Order();
        CreateOrderDTO createOrderDTO = new CreateOrderDTO("s", "f", null);

        when(orderAdminRepository.changeRoute(anyLong(), any(CreateOrderDTO.class))).thenReturn(ResponseEntity.ok(order));

        Assertions.assertThat(adminsService.changeRoute(anyLong(), any(CreateOrderDTO.class))).isEqualTo(ResponseEntity.ok(order));
        Mockito.verify(orderAdminRepository, times(1)).changeRoute(anyLong(), Matchers.any(CreateOrderDTO.class));
    }

    @Test
    public void userHistory() {
        List<Order> orderList = new ArrayList<>();

        when(orderAdminRepository.userHistory(anyLong(), any(Pageable.class))).thenReturn(orderList);

        Assertions.assertThat(adminsService.userHistory(anyLong(), any(Pageable.class))).isEmpty();
        Mockito.verify(orderAdminRepository, times(1)).userHistory(anyLong(), any(Pageable.class));
    }

    @Test
    public void rejectDriver() {
        Order order = new Order();

        when(orderAdminRepository.rejectDriver(anyLong())).thenReturn(ResponseEntity.ok(order));

        Assertions.assertThat(adminsService.rejectDriver(anyLong())).isEqualTo(ResponseEntity.ok(order));
        Mockito.verify(orderAdminRepository, times(1)).rejectDriver(anyLong());
    }
}
