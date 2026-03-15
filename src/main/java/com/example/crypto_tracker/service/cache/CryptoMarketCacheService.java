package com.example.crypto_tracker.service.cache;

import com.example.crypto_tracker.dto.crypto.DashboardCurrencyDto;
import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoMarketCacheService {

    private static final Duration PRICE_TTL = Duration.ofMinutes(15);
    private static final Duration DASHBOARD_TTL = Duration.ofMinutes(15);
    private static final String DASHBOARD_KEY = "dashboard:currencies";
    private static final String PRICE_KEY_PREFIX = "price:";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

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

        try {
            String json = objectMapper.writeValueAsString(dashboardData);
            stringRedisTemplate.opsForValue().set(DASHBOARD_KEY, json, DASHBOARD_TTL);

            log.info("Dashboard cached under key {}", DASHBOARD_KEY);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to cache dashboard data in Redis", e);
        }
    }

    public List<DashboardCurrencyDto> getDashboard() {
        String json = stringRedisTemplate.opsForValue().get(DASHBOARD_KEY);

        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(
                    json.getBytes(StandardCharsets.UTF_8),
                    new TypeReference<List<DashboardCurrencyDto>>() {}
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read dashboard data from Redis", e);
        }
    }

    public void cachePrices(List<ExternalCryptoDto> externalData) {
        externalData.forEach(dto ->
                stringRedisTemplate.opsForValue().set(
                        buildPriceKey(dto.getApiId()),
                        dto.getCurrentPrice().toPlainString(),
                        PRICE_TTL
                )
        );

        log.info("Cached prices for {} cryptocurrencies", externalData.size());
    }

    public BigDecimal getPrice(String apiId) {
        String value = stringRedisTemplate.opsForValue().get(buildPriceKey(apiId));

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid price format in Redis for apiId: " + apiId, e);
        }
    }

    private String buildPriceKey(String apiId) {
        return PRICE_KEY_PREFIX + apiId;
    }
}