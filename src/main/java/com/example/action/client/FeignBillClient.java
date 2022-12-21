package com.example.action.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "feignBill", url = "http://localhost:8083/bill")
public interface FeignBillClient {

    @GetMapping
    Double getBill();

    @PostMapping
    void payment(); // will be change
}