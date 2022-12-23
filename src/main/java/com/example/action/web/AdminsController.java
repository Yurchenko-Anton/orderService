package com.example.action.web;

import com.example.action.dto.CreateOrderDTO;
import com.example.action.dto.Order;
import com.example.action.service.AdminsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('users:admin')")
@RequiredArgsConstructor
public class AdminsController {

    private final AdminsService adminsService;

    @PostMapping
    public ResponseEntity<CreateOrderDTO> createOrderToUser(@RequestBody CreateOrderDTO createOrderDTO) {
        return adminsService.createOrder(createOrderDTO);
    }

    @GetMapping
    public List<Order> getActiveOrders() {
        return adminsService.activeOrders();
    }

    @PostMapping("/finish/{orderId}")
    public ResponseEntity<Order> finishOrder(@PathVariable Long orderId) {
        return adminsService.finishOrder(orderId);
    }

    @PostMapping("/change/{orderId}")
    public ResponseEntity<Order> changeRoute(@PathVariable Long orderId, @RequestBody CreateOrderDTO createOrderDTO) {
        return adminsService.changeRoute(orderId, createOrderDTO);
    }

    @GetMapping("/history/{userId}")
    public List<Order> getUserHistory(@PathVariable Long userId, Pageable pageable) {
        return adminsService.userHistory(userId, pageable);
    }

    @PostMapping("/reject/{orderId}")
    public ResponseEntity<Order> rejectDriver(@PathVariable Long orderId) {
        return adminsService.rejectDriver(orderId);
    }
}