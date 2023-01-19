package com.example.action.cache;

import com.example.action.repository.PromoConfigRepository;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
@AllArgsConstructor
public class PromoConfigCachingTest {

    @Mock
    private DSLContext dslContext;

    @Mock
    private PromoConfigRepository mock;

    private final static String CACHE_KEY = "test_promo_config";

    @EnableCaching
    @Configuration
    public static class PromoConfigCacheTest {

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("promo_test");
        }
    }

    @Test
    public void getCachedPromoConfig() {
        mock.getPromoConfigs(CACHE_KEY);
        mock.getPromoConfigs(CACHE_KEY);

        verify(dslContext);
    }
}