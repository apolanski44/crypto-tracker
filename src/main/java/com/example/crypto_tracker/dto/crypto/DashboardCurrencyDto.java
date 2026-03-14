package com.example.crypto_tracker.dto.crypto;

import java.math.BigDecimal;

public record DashboardCurrencyDto(
        String apiId,
        String symbol,
        String name,
        String image,
        BigDecimal currentPrice
) {
}
