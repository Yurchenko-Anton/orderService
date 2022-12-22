package com.example.action.service;

import com.example.action.client.FeignBillClient;
import com.example.action.client.FeignDistanceClient;
import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderPassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengersService {

    private final OrderPassengerRepository orderRepository;

    private final FeignDistanceClient feignDistanceClient;

    private final FeignBillClient feignBillClient;

    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<CreateOrderDTO> createOrder(String token, CreateOrderDTO createOrderDTO) {
        Long id;
        Double distance = feignDistanceClient.getDistance(createOrderDTO);
        id = createOrderDTO.getGuestId() != null ? createOrderDTO.getGuestId() : getUserId(token);
        return orderRepository.createOrder(id, createOrderDTO, distance);
    }

    public List<Order> getAllUserOrders(String token, Pageable pageable) {
        Long id = getUserId(token);
        return orderRepository.userOrders(id, pageable);
    }

    public List<Order> getActiveOrder(String token) {
        Long id = getUserId(token);
        return orderRepository.activeOrder(id);
    }

    public Order finishOrder(String token, long orderId, int rating) {
        Long id = getUserId(token);
        Order order = orderRepository.finishOrder(id, orderId, rating);
        feignBillClient.payment();
        return order;
    }

    private Long getUserId(String token) {
        String id = jwtTokenProvider.decodeToken(token).getSubject();
        return Long.parseLong(id);
    }
}
