package com.example.action.web;

import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.service.PassengersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
@PreAuthorize("hasAuthority('users:passenger')")
@RequiredArgsConstructor
public class PassengersController {

    private final PassengersService passengersService;

    private static final String HEADER = "Authorization";

    @PostMapping
    public ResponseEntity<CreateOrderDTO> createOrder(@RequestHeader(HEADER) String token, @RequestBody CreateOrderDTO createOrderDTO) {
        return passengersService.createOrder(token, createOrderDTO);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@RequestHeader(HEADER) String token, Pageable pageable) {
        return ResponseEntity.ok(passengersService.getAllUserOrders(token, pageable));
    }

    @GetMapping("/active")
    public List<Order> getActiveOrder(@RequestHeader(HEADER) String token){
        return passengersService.getActiveOrder(token);
    }

    @PostMapping("/{orderId}/{rating}")
    public Order finishOrder(@RequestHeader(HEADER) String token,@PathVariable Long orderId, @PathVariable int rating) {
        return passengersService.finishOrder(token, orderId, rating);
    }

}