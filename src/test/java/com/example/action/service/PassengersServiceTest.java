package com.example.action.service;

import com.example.action.client.FeignBillClient;
import com.example.action.client.FeignDistanceClient;
import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.OrderPassengerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.mockito.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@AllArgsConstructor
public class PassengersServiceTest {

    @Mock
    private OrderPassengerRepository orderPassengerRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private FeignDistanceClient feignDistanceClient;

    @Mock
    private FeignBillClient feignBillClient;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private PassengersService passengersService;

    private final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxNDc1MDUxLCJleHAiOjE2NzE0Nzg2NTF9.jqqOLZl9C61rMbhnCx3pZNEx1IjLoS-NRkXliHHROCU";

    private final Claims CLAIMS = Jwts.claims().setSubject("1");

    @Test
    public void shouldCreateOrder() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO("startPosition", "finishPosition", null);

        when(feignDistanceClient.getDistance(Matchers.any(CreateOrderDTO.class))).thenReturn(100.0);
        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderPassengerRepository.createOrder(anyLong(), any(CreateOrderDTO.class), anyDouble())).thenReturn(ResponseEntity.ok(createOrderDTO));

        assertThat(passengersService.createOrder(JWT_TOKEN, createOrderDTO)).isEqualTo(ResponseEntity.ok(createOrderDTO));
        verify(orderPassengerRepository, times(1)).createOrder(anyLong(), Matchers.any(CreateOrderDTO.class), anyDouble());

    }

    @Test
    public void shouldGetAllUserOrders() {
        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);

        assertThat(passengersService.getAllUserOrders(JWT_TOKEN, Matchers.any(Pageable.class))).isEmpty();
        verify(orderHistoryRepository, times(1)).userOrders(anyLong(), Matchers.any(Pageable.class));
    }

    @Test
    public void shouldGetActiveOrder() {
        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);

        assertThat(passengersService.getActiveOrder(JWT_TOKEN)).isEmpty();
        verify(orderPassengerRepository, times(1)).activeOrder(anyLong());
    }

    @Test
    public void shouldFinishOrder() {
        Order order = new Order();

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        doCallRealMethod().when(orderHistoryRepository).addToHistory(order);
        doCallRealMethod().when(orderPassengerRepository).deleteFinishedOrder(order);

        assertThat(passengersService.finishOrder(JWT_TOKEN, 1L, 5)).isNull();
        verify(orderPassengerRepository, times(1)).finishOrder(anyLong(), anyLong(), anyInt());
        verify(orderHistoryRepository,times(1)).addToHistory(any(Order.class));
    }
}
