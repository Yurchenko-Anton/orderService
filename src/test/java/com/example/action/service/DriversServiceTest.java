package com.example.action.service;

import com.example.action.client.FeignBillClient;
import com.example.action.dto.Order;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderDriverRepository;
import com.example.action.repository.OrderHistoryRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
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
public class DriversServiceTest {

    @Mock
    private OrderDriverRepository orderRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private FeignBillClient feignBillClient;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private DriversService driversService;

    private final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxNDc1MDUxLCJleHAiOjE2NzE0Nzg2NTF9.jqqOLZl9C61rMbhnCx3pZNEx1IjLoS-NRkXliHHROCU";

    private final Claims CLAIMS = Jwts.claims().setSubject("1");

    @Test
    public void shouldGetEmptyOrders() {
        List<Order> orders = new ArrayList<>();

        when(orderRepository.emptyOrders()).thenReturn(orders);

        assertThat(driversService.emptyOrders()).isEmpty();
        verify(orderRepository, Mockito.times(1)).emptyOrders();
    }

    @Test
    public void shouldAcceptOrder() {
        Order order = new Order();

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderRepository.acceptOrder(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(order));

        assertThat(driversService.acceptOrder(anyLong(), JWT_TOKEN)).isEqualTo(ResponseEntity.ok(order));
        verify(orderRepository, times(1)).acceptOrder(anyLong(), anyLong());
    }

    @Test
    public void shouldGetAvgDriverRating() {

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderHistoryRepository.avgRating(anyLong())).thenReturn(ResponseEntity.ok(4.5));

        assertThat(driversService.avgRating(JWT_TOKEN)).isEqualTo(ResponseEntity.ok(4.5));
        verify(orderHistoryRepository, times(1)).avgRating(anyLong());
    }

    @Test
    public void shouldFinishOrder() {
        Order order = new Order();
        Double bill = 100.0;

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(feignBillClient.getBill()).thenReturn(bill);
        when(orderRepository.finishDriverOrder(anyLong())).thenReturn(ResponseEntity.ok(order));

        assertThat(driversService.finishOrder(JWT_TOKEN)).isEqualTo(ResponseEntity.ok(order));
        verify(orderRepository, times(1)).finishDriverOrder(anyLong());

    }
}
