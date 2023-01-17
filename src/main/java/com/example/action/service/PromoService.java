package com.example.action.service;

import com.example.action.dto.Promo;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.model.PromoType;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Promo> getAllPromo(String token) {
        addPromoIfPossible(token);

        Long userId = getUserId(token);

        return promoRepository.getPromos(userId);
    }

    public List<Promo> activatePromo(String token, List<Promo> promoId) {
        long userId = getUserId(token);

        promoId.forEach(promo -> promoRepository.activationPromo(userId, promo.getId()));

        return promoRepository.getActivatedUserPromo(userId);
    }

    private void addPromoIfPossible(String token) {
        Long userId = getUserId(token);

        int tripsCount = orderHistoryRepository.userOrdersSize(userId);

        PromoType promoType = suitablePromo(tripsCount);

        if (checkIfNotExist(userId, promoType)) {
            promoRepository.addPromo(userId, promoType);
        }
    }

    private Long getUserId(String token) {
        String id = jwtTokenProvider.decodeToken(token).getSubject();
        return Long.parseLong(id);
    }

    private PromoType suitablePromo(int tripsCount) {
        List<PromoType> promoType = Arrays.stream(PromoType.values())
                .filter(promo -> promo.getNumberTrips().equals(tripsCount)).collect(Collectors.toList());

        return promoType.isEmpty() ? null : promoType.stream().findFirst().get();
    }

    private boolean checkIfNotExist(Long userId, PromoType promoType) {
        return promoType != null && promoRepository.checkIfNotExist(userId, promoType);
    }
}