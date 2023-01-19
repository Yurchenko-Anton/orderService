package com.example.action.service;

import com.example.action.dto.PromoConfigDTO;
import com.example.action.dto.UserPromoDTO;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.model.PromoType;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoConfigRepository;
import com.example.action.repository.UserPromoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPromoService {

    private final OrderHistoryRepository orderHistoryRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserPromoRepository userPromoRepository;

    private final PromoConfigRepository promoConfigRepository;

    private final static String CACHE_KEY = "promo_config";

    public List<UserPromoDTO> getAllUserPromo(String token) {
        tryGeneratePromoCode(token);

        Long userId = getUserId(token);

        return userPromoRepository.getAllUserPromo(userId);
    }

    public List<UserPromoDTO> activateUserPromo(String token, List<UserPromoDTO> userPromoDTO) {
        long userId = getUserId(token);

        userPromoDTO.forEach(userPromoDTOId -> userPromoRepository.activationPromo(userId, userPromoDTOId.getId()));

        return userPromoRepository.getActivatedUserPromo(userId);
    }

    public ResponseEntity<PromoConfigDTO> createNewPromoType(PromoConfigDTO promoTypeDTO) {
        if (!filterUnApplicablePromoTypes(promoTypeDTO)) {
            promoConfigRepository.createPromoConfig(promoTypeDTO);
            return ResponseEntity.ok(promoTypeDTO);
        } else return ResponseEntity.badRequest().build();
    }

    private void tryGeneratePromoCode(String token) {
        Long userId = getUserId(token);

        int tripsCount = orderHistoryRepository.getUserOrdersQuantity(userId);

        getPromoCodesByTripsCount(tripsCount)
                .stream()
                .filter(promoTypeDTO -> checkIfNotExist(userId, promoTypeDTO))
                .forEach(promoTypeDTO -> userPromoRepository.addPromoToUser(userId, promoTypeDTO));
    }

    private Long getUserId(String token) {
        String id = jwtTokenProvider.decodeToken(token).getSubject();
        return Long.parseLong(id);
    }

    private List<PromoConfigDTO> getPromoCodesByTripsCount(int tripsCount) {
        return promoConfigRepository.getPromoConfigs(CACHE_KEY)
                .stream()
                .filter(promoTypeDTO -> promoTypeDTO.getTripsCount() <= tripsCount)
                .collect(Collectors.toList());
    }

    private boolean checkIfNotExist(Long userId, PromoConfigDTO promoConfigDTO) {
        return promoConfigDTO != null && userPromoRepository.checkIfNotExist(userId, promoConfigDTO);
    }

    private boolean filterUnApplicablePromoTypes(PromoConfigDTO promoConfigDTO) {
        return Arrays.stream(PromoType.values())
                .noneMatch(promoType -> promoType.name().equals(promoConfigDTO.getPromoType()));
    }
}