package com.example.crypto_tracker.service.crypto.dashboard;

import com.example.crypto_tracker.dto.crypto.DashboardCurrencyDto;
import com.example.crypto_tracker.repository.CryptoCurrencyRepository;
import com.example.crypto_tracker.service.cache.CryptoMarketCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoDashboardService {
    private final CryptoMarketCacheService cryptoMarketCacheService;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    public List<DashboardCurrencyDto> getDashboardCurrencies() {
        List<DashboardCurrencyDto> cachedDashboard = cryptoMarketCacheService.getDashboard();

        if (cachedDashboard != null && !cachedDashboard.isEmpty()) {
            return cachedDashboard;
        }

        log.warn("Dashboard cache is empty, building fallback response");

        return cryptoCurrencyRepository.findAllByOrderByNameAsc().stream()
                .map(currency -> new DashboardCurrencyDto(
                        currency.getApiId(),
                        currency.getSymbol(),
                        currency.getName(),
                        currency.getImage(),
                        cryptoMarketCacheService.getPrice(currency.getApiId())
                ))
                .toList();
    }
}
