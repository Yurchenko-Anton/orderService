package com.example.action.web;

import com.example.action.dto.PromoConfigDTO;
import com.example.action.dto.UserPromoDTO;
import com.example.action.service.UserPromoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promo")
@PreAuthorize("hasAuthority('users:passenger')")
@RequiredArgsConstructor
public class UserPromoController {

    private static final String HEADER = "Authorization";

    private final UserPromoService userPromoService;

    @GetMapping
    public List<UserPromoDTO> getAllUserPromo(@RequestHeader(HEADER) String token) {
        return userPromoService.getAllUserPromo(token);
    }

    @PostMapping()
    public List<UserPromoDTO> activatePromo(@RequestHeader(HEADER) String token, @RequestBody List<UserPromoDTO> promoId) {
        return userPromoService.activateUserPromo(token, promoId);
    }

    @PreAuthorize("hasAuthority('users:admin')")
    @PostMapping("/new")
    public ResponseEntity<PromoConfigDTO> createNewPromoType(@RequestBody PromoConfigDTO promoTypeDTO) {
        return userPromoService.createNewPromoType(promoTypeDTO);
    }
}