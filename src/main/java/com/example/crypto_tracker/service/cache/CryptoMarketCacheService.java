package com.example.crypto_tracker.service.cache;

import com.example.crypto_tracker.dto.crypto.DashboardCurrencyDto;
import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoMarketCacheService {
    private static final Duration PRICE_TTL = Duration.ofMinutes(15);
    private static final Duration DASHBOARD_TTL = Duration.ofMinutes(15);
    private static final String DASHBOARD_KEY = "dashboard:currencies";

    private final RedisTemplate<String, Object> redisTemplate;

    public void cachePrices(List<ExternalCryptoDto> externalData) {
        externalData.forEach(dto ->{
            String key = buildPriceKey(dto.getApiId());
            redisTemplate.opsForValue().set(key, dto.getCurrentPrice(), PRICE_TTL);
        });
    }

    public void cacheDashboard(List<ExternalCryptoDto> externalData) {
        List<DashboardCurrencyDto> dashboardData = externalData.stream()
                .map(dto -> new DashboardCurrencyDto(
                        dto.getApiId(),
                        dto.getSymbol(),
                        dto.getName(),
                        dto.getImage(),
                        dto.getCurrentPrice()
                ))
                .toList();

        redisTemplate.opsForValue().set(DASHBOARD_KEY, dashboardData, DASHBOARD_TTL);
    }

    public BigDecimal getPrice(String apiId) {
        Object value = redisTemplate.opsForValue().get(buildPriceKey(apiId));

        return value instanceof BigDecimal bigDecimal ? bigDecimal : null;
    }

    public List<DashboardCurrencyDto> getDashboard() {
        Object value = redisTemplate.opsForValue().get(DASHBOARD_KEY);

        return value instanceof List<?> list ?  (List<DashboardCurrencyDto>) list : null;
    }

    private String buildPriceKey(String apiId) {
        return "price:" + apiId;
    }
}
