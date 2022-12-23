package com.example.action.client;

import com.example.action.dto.CreateOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "feignDistance", url = "http://localhost:8084/distance")
public interface FeignDistanceClient {

    @GetMapping
    Double getDistance(@RequestBody CreateOrderDTO createOrderDTO);
}