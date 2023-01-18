package com.example.action.service;

import com.example.action.dto.Promo;
import com.example.action.dto.PromoTypeDTO;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.model.PromoType;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoRepository;
import com.example.action.repository.PromoTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromoService {

    private final OrderHistoryRepository orderHistoryRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PromoRepository promoRepository;

    private final PromoTypeRepository promoTypeRepository;

    private final PromoCacheService promoCacheService;

    public List<Promo> getAllPromo(String token) {
        setPromoCache();

        addPromoIfPossible(token);

        Long userId = getUserId(token);

        return promoRepository.getPromos(userId);
    }

    public List<Promo> activatePromo(String token, List<Promo> promoId) {
        long userId = getUserId(token);

        promoId.forEach(promo -> promoRepository.activationPromo(userId, promo.getId()));

        return promoRepository.getActivatedUserPromo(userId);
    }

    public ResponseEntity<PromoTypeDTO> createNewPromo(PromoTypeDTO promoTypeDTO) {
        if (!validatePromoType(promoTypeDTO)) {
            promoTypeRepository.createPromoType(promoTypeDTO);
            return ResponseEntity.ok(promoTypeDTO);
        } else return ResponseEntity.badRequest().build();
    }

    private void setPromoCache() {
        List<PromoTypeDTO> promoTypeDTOList = promoTypeRepository.getPromoTypes();
        promoCacheService.addValuesToCache(promoTypeDTOList);
    }

    private void addPromoIfPossible(String token) {
        Long userId = getUserId(token);

        int tripsCount = orderHistoryRepository.userOrdersSize(userId);

        suitablePromo(tripsCount)
                .stream()
                .filter(promoTypeDTO -> checkIfNotExist(userId, promoTypeDTO))
                .forEach(promoTypeDTO -> promoRepository.addPromo(userId, promoTypeDTO));
    }

    private Long getUserId(String token) {
        String id = jwtTokenProvider.decodeToken(token).getSubject();
        return Long.parseLong(id);
    }

    private List<PromoTypeDTO> suitablePromo(int tripsCount) {
        return promoCacheService.getCacheValues()
                .stream()
                .filter(promoTypeDTO -> promoTypeDTO.getTripsCount() <= tripsCount)
                .collect(Collectors.toList());
    }

    private boolean checkIfNotExist(Long userId, PromoTypeDTO promoTypeDTO) {
        return promoTypeDTO != null && promoRepository.checkIfNotExist(userId, promoTypeDTO);
    }

    private boolean validatePromoType(PromoTypeDTO promoTypeDTO) {
        return Arrays.stream(PromoType.values())
                .noneMatch(promoType -> promoType.name().equals(promoTypeDTO.getPromoType()));
    }
}