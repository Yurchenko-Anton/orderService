package com.example.action.service;

import com.example.action.dto.PromoTypeDTO;
import com.example.action.dto.UsersPromoDTO;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoTypeRepository;
import com.example.action.repository.UsersPromoRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@AllArgsConstructor
public class UsersUsersPromoServiceTestDTO {

    private final static String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxNDc1MDUxLCJleHAiOjE2NzE0Nzg2NTF9.jqqOLZl9C61rMbhnCx3pZNEx1IjLoS-NRkXliHHROCU";

    private final static Claims CLAIMS = Jwts.claims().setSubject("1");

    private final static Integer TRIPS_SIZE = 1;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UsersPromoRepository usersPromoRepository;

    @Mock
    private PromoCacheService promoCacheService;

    @Mock
    private PromoTypeRepository promoTypeRepository;

    @InjectMocks
    private UsersPromoService usersPromoService;

    @Test
    public void shouldGetPromo() {
        List<PromoTypeDTO> promoTypeDTOList = new ArrayList<>();

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderHistoryRepository.userOrdersSize(anyLong())).thenReturn(TRIPS_SIZE);
        when(promoTypeRepository.getPromoTypes()).thenReturn(promoTypeDTOList);

        assertThat(usersPromoService.getAllUserPromo(JWT_TOKEN)).isEmpty();
        verify(usersPromoRepository).getAllUserPromo(anyLong());
    }

    @Test
    public void shouldActivatePromo() {
        List<UsersPromoDTO> promoIdList = new ArrayList<>();
        promoIdList.add(new UsersPromoDTO(1, 50));
        promoIdList.add(new UsersPromoDTO(2, 50));

        doNothing().when(usersPromoRepository).activationPromo(anyLong(), anyInt());
        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);

        assertThat(usersPromoService.activateUserPromo(JWT_TOKEN, promoIdList)).isEmpty();
        verify(usersPromoRepository, times(2)).activationPromo(anyLong(), anyInt());
    }
}