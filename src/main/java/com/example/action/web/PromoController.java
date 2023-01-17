package com.example.action.web;

import com.example.action.dto.Promo;
import com.example.action.service.PromoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promo")
@PreAuthorize("hasAuthority('users:passenger')")
@RequiredArgsConstructor
public class PromoController {

    private static final String HEADER = "Authorization";

    private final PromoService promoService;

    @GetMapping
    public List<Promo> getAllPromo(@RequestHeader(HEADER) String token) {
        return promoService.getAllPromo(token);
    }

    @PostMapping()
    public List<Promo> activatePromo(@RequestHeader(HEADER) String token, @RequestBody List<Promo> promoId) {
        return promoService.activatePromo(token, promoId);
    }
}