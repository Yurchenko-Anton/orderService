package com.example.action.service;

import com.example.action.dto.CacheDTO;
import com.example.action.dto.PromoConfigDTO;
import com.example.action.dto.UserPromoDTO;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoConfigRepository;
import com.example.action.repository.UserPromoRepository;
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
public class UserPromoServiceTest {

    private final static String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxNDc1MDUxLCJleHAiOjE2NzE0Nzg2NTF9.jqqOLZl9C61rMbhnCx3pZNEx1IjLoS-NRkXliHHROCU";

    private final static Claims CLAIMS = Jwts.claims().setSubject("1");

    private final static Integer TRIPS_SIZE = 1;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserPromoRepository userPromoRepository;

    @Mock
    private PromoConfigRepository promoConfigRepository;

    @InjectMocks
    private UserPromoService userPromoService;

    @Test
    public void shouldGetPromo() {
        List<PromoConfigDTO> promoConfigDTOList = new ArrayList<>();

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderHistoryRepository.getUserOrdersQuantity(anyLong())).thenReturn(TRIPS_SIZE);
        when(promoConfigRepository.getPromoConfigs(anyString())).thenReturn(promoConfigDTOList);

        assertThat(userPromoService.getAllUserPromo(JWT_TOKEN)).isEmpty();
        verify(userPromoRepository).getAllUserPromo(anyLong());
    }

    @Test
    public void shouldActivatePromo() {
        List<UserPromoDTO> promoIdList = new ArrayList<>();
        promoIdList.add(new UserPromoDTO(1, 50));
        promoIdList.add(new UserPromoDTO(2, 50));

        doNothing().when(userPromoRepository).activationPromo(anyLong(), anyInt());
        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);

        assertThat(userPromoService.activateUserPromo(JWT_TOKEN, promoIdList)).isEmpty();
        verify(userPromoRepository, times(2)).activationPromo(anyLong(), anyInt());
    }
}