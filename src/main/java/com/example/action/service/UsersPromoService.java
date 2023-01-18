package com.example.action.service;

import com.example.action.dto.PromoTypeDTO;
import com.example.action.dto.UsersPromoDTO;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.model.PromoType;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoTypeRepository;
import com.example.action.repository.UsersPromoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersPromoService {

    private final OrderHistoryRepository orderHistoryRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final UsersPromoRepository usersPromoRepository;

    private final PromoTypeRepository promoTypeRepository;

    private final PromoCacheService promoCacheService;

    public List<UsersPromoDTO> getAllUserPromo(String token) {
        setPromoCache();

        givePromoToUserIfPossible(token);

        Long userId = getUserId(token);

        return usersPromoRepository.getAllUserPromo(userId);
    }

    public List<UsersPromoDTO> activateUserPromo(String token, List<UsersPromoDTO> usersPromoDTO) {
        long userId = getUserId(token);

        usersPromoDTO.forEach(usersPromoDTOId -> usersPromoRepository.activationPromo(userId, usersPromoDTOId.getId()));

        return usersPromoRepository.getActivatedUserPromo(userId);
    }

    public ResponseEntity<PromoTypeDTO> createNewPromoType(PromoTypeDTO promoTypeDTO) {
        if (!validatePromoType(promoTypeDTO)) {
            promoTypeRepository.createPromoType(promoTypeDTO);
            return ResponseEntity.ok(promoTypeDTO);
        } else return ResponseEntity.badRequest().build();
    }

    private void setPromoCache() {
        List<PromoTypeDTO> promoTypeDTOList = promoTypeRepository.getPromoTypes();
        promoCacheService.addValuesToCache(promoTypeDTOList);
    }

    private void givePromoToUserIfPossible(String token) {
        Long userId = getUserId(token);

        int tripsCount = orderHistoryRepository.userOrdersSize(userId);

        suitablePromoToUserByTripsCount(tripsCount)
                .stream()
                .filter(promoTypeDTO -> checkIfNotExist(userId, promoTypeDTO))
                .forEach(promoTypeDTO -> usersPromoRepository.addPromoToUser(userId, promoTypeDTO));
    }

    private Long getUserId(String token) {
        String id = jwtTokenProvider.decodeToken(token).getSubject();
        return Long.parseLong(id);
    }

    private List<PromoTypeDTO> suitablePromoToUserByTripsCount(int tripsCount) {
        return promoCacheService.getCacheValues()
                .stream()
                .filter(promoTypeDTO -> promoTypeDTO.getTripsCount() <= tripsCount)
                .collect(Collectors.toList());
    }

    private boolean checkIfNotExist(Long userId, PromoTypeDTO promoTypeDTO) {
        return promoTypeDTO != null && usersPromoRepository.checkIfNotExist(userId, promoTypeDTO);
    }

    private boolean validatePromoType(PromoTypeDTO promoTypeDTO) {
        return Arrays.stream(PromoType.values())
                .noneMatch(promoType -> promoType.name().equals(promoTypeDTO.getPromoType()));
    }
}