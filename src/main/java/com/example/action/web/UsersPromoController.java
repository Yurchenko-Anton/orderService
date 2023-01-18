package com.example.action.web;

import com.example.action.dto.PromoTypeDTO;
import com.example.action.dto.UsersPromoDTO;
import com.example.action.service.UsersPromoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promo")
@PreAuthorize("hasAuthority('users:passenger')")
@RequiredArgsConstructor
public class UsersPromoController {

    private static final String HEADER = "Authorization";

    private final UsersPromoService usersPromoService;

    @GetMapping
    public List<UsersPromoDTO> getAllUserPromo(@RequestHeader(HEADER) String token) {
        return usersPromoService.getAllUserPromo(token);
    }

    @PostMapping()
    public List<UsersPromoDTO> activatePromo(@RequestHeader(HEADER) String token, @RequestBody List<UsersPromoDTO> promoId) {
        return usersPromoService.activateUserPromo(token, promoId);
    }

    @PreAuthorize("hasAuthority('users:admin')")
    @PostMapping("/new")
    public ResponseEntity<PromoTypeDTO> createNewPromoType(@RequestBody PromoTypeDTO promoTypeDTO) {
        return usersPromoService.createNewPromoType(promoTypeDTO);
    }
}