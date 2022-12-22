package com.example.action.service;

import com.example.action.client.FeignBillClient;
import com.example.action.dto.Order;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderDriverRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@AllArgsConstructor
public class DriversServiceTest {

    @Mock
    private OrderDriverRepository orderRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private FeignBillClient feignBillClient;

    @InjectMocks
    private DriversService driversService;

    private final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxNDc1MDUxLCJleHAiOjE2NzE0Nzg2NTF9.jqqOLZl9C61rMbhnCx3pZNEx1IjLoS-NRkXliHHROCU";

    private final Claims CLAIMS = Jwts.claims().setSubject("1");

    @Test
    public void emptyOrders() {
        List<Order> orderList = new ArrayList<>();

        when(orderRepository.emptyOrders()).thenReturn(orderList);

        Assertions.assertThat(driversService.emptyOrders()).isEmpty();
        Mockito.verify(orderRepository, Mockito.times(1)).emptyOrders();
    }

    @Test
    public void acceptOrder() {
        Order order = new Order();

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderRepository.acceptOrder(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(order));

        Assertions.assertThat(driversService.acceptOrder(anyLong(), JWT_TOKEN)).isEqualTo(ResponseEntity.ok(order));
        Mockito.verify(orderRepository, times(1)).acceptOrder(anyLong(), anyLong());
    }

    @Test
    public void avgRating() {

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderRepository.avgRating(anyLong())).thenReturn(ResponseEntity.ok(4.5));

        Assertions.assertThat(driversService.avgRating(JWT_TOKEN)).isEqualTo(ResponseEntity.ok(4.5));
        Mockito.verify(orderRepository, times(1)).avgRating(anyLong());
    }

    @Test
    public void finishOrder() {
        Order order = new Order();

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderRepository.finishDriverOrder(anyLong())).thenReturn(ResponseEntity.ok(order));

        Assertions.assertThat(driversService.finishOrder(JWT_TOKEN)).isEqualTo(ResponseEntity.ok(order));
        Mockito.verify(orderRepository, times(1)).finishDriverOrder(anyLong());

    }
}
