package com.example.action.service;

import com.example.action.client.FeignBillClient;
import com.example.action.dto.Order;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderDriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriversService {

    private final OrderDriverRepository orderRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final FeignBillClient feignBillClient;

    public List<Order> emptyOrders() {
        return orderRepository.emptyOrders();
    }

    public ResponseEntity<Order> acceptOrder(Long orderId, String token) {
        Long id = getUserId(token);
        return orderRepository.acceptOrder(orderId, id);
    }

    public ResponseEntity<Double> avgRating(String token) {
        Long id = getUserId(token);
        return orderRepository.avgRating(id);
    }

    public ResponseEntity<Order> finishOrder(String token) {
        Long id = getUserId(token);
        feignBillClient.getBill();
        return orderRepository.finishDriverOrder(id);
    }

    private Long getUserId(String token) {
        String id = jwtTokenProvider.decodeToken(token).getSubject();
        return Long.parseLong(id);
    }
}