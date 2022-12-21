package com.example.action.service;

import com.example.action.client.FeignDistanceClient;
import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.repository.OrderAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminsService {

    private final OrderAdminRepository orderAdminRepository;

    private final FeignDistanceClient feignDistanceClient;

    public ResponseEntity<CreateOrderDTO> createOrder(CreateOrderDTO createOrderDTO) {
        Double distance = feignDistanceClient.getDistance(createOrderDTO);
        return orderAdminRepository.createOrder(createOrderDTO, distance);
    }

    public List<Order> activeOrders() {
        return orderAdminRepository.activeOrder();
    }

    public ResponseEntity<Order> finishOrder(Long id) {
        return orderAdminRepository.finishDriverOrder(id);
    }

    public ResponseEntity<Order> changeRoute(Long id, CreateOrderDTO createOrderDTO) {
        return orderAdminRepository.changeRoute(id, createOrderDTO);
    }

    public List<Order> userHistory(Long id, Pageable pageable) {
        return orderAdminRepository.userHistory(id, pageable);
    }

    public ResponseEntity<Order> rejectDriver(Long id) {
        return orderAdminRepository.rejectDriver(id);
    }
}