package com.example.action.jwt;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    private final String NOT_VALID_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcwOTQxMjYxLCJleHAiOjE2NzA5NDQ4NjF9.Xs8uVdan1rvlCoiipZnDNDl85hKxJ5KW8o_wXRxYfII";
    private final String VALID_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwicGhvbmUiOiIrMzgwOTM3Nzc3Nzc3IiwiaWF0IjoxNjcxMDE0MTYxLCJleHAiOjE2NzEwMTc3NjF9.k1Yk8fy20Ief1bgf-7JLhJtLtVZYiyTryZlJYUVgV_M";

    @Test(expected = JwtAuthenticationException.class)
    public void validateNotValidToken() throws JwtAuthenticationException {
        jwtTokenProvider.validateToken(NOT_VALID_JWT);
    }

    @Test
    public void validateToken() throws JwtAuthenticationException {
        Assert.assertTrue(jwtTokenProvider.validateToken(VALID_JWT));
    }
}
