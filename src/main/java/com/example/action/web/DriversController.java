package com.example.action.web;

import com.example.action.dto.Order;
import com.example.action.service.DriversService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
@PreAuthorize("hasAuthority('users:driver')")
@RequiredArgsConstructor
public class DriversController {

    private final DriversService driversService;

    private static final String HEADER = "Authorization";

    @GetMapping
    public List<Order> getEmptyOrder() {
        return driversService.emptyOrders();
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<Order> acceptOrder(@RequestHeader(HEADER) String token, @PathVariable Long id) {
        return driversService.acceptOrder(id, token);
    }

    @GetMapping("/rating")
    public ResponseEntity<Double> getAvgRating(@RequestHeader(HEADER) String token) {
        return driversService.avgRating(token);
    }

    @PostMapping("/finish")
    public ResponseEntity<Order> finishOrder(@RequestHeader(HEADER) String token) {
        return driversService.finishOrder(token);
    }
}
