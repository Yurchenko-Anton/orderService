package com.example.action.service;

import com.example.action.dto.Promo;
import com.example.action.jwt.JwtTokenProvider;
import com.example.action.repository.OrderHistoryRepository;
import com.example.action.repository.PromoRepository;
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
public class PromoServiceTest {

    private final static String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxNDc1MDUxLCJleHAiOjE2NzE0Nzg2NTF9.jqqOLZl9C61rMbhnCx3pZNEx1IjLoS-NRkXliHHROCU";

    private final static Claims CLAIMS = Jwts.claims().setSubject("1");

    private final static Integer TRIPS_SIZE = 1;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PromoRepository promoRepository;

    @InjectMocks
    private PromoService promoService;

    @Test
    public void shouldGetPromo() {
        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);
        when(orderHistoryRepository.userOrdersSize(anyLong())).thenReturn(TRIPS_SIZE);

        assertThat(promoService.getAllPromo(JWT_TOKEN)).isEmpty();
        verify(promoRepository).getPromos(anyLong());
    }

    @Test
    public void shouldActivatePromo() {
        List<Promo> promoIdList = new ArrayList<>();
        promoIdList.add(new Promo(1, anyInt()));
        promoIdList.add(new Promo(2, anyInt()));

        when(jwtTokenProvider.decodeToken(anyString())).thenReturn(CLAIMS);

        assertThat(promoService.activatePromo(JWT_TOKEN, promoIdList)).isEmpty();
        verify(promoRepository, times(2)).activationPromo(anyLong(), anyInt());
    }
}