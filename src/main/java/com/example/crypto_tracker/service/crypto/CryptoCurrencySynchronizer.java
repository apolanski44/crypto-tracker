package com.example.crypto_tracker.service.crypto;

import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import com.example.crypto_tracker.model.CryptoCurrency;
import com.example.crypto_tracker.repository.CryptoCurrencyRepository;
import com.example.crypto_tracker.service.cache.CryptoMarketCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoCurrencySynchronizer {
    //TODO: Remove logs after implementing tests
    private final CryptoCurrencyRepository repository;
    private final CryptoCurrencyMapper cryptoCurrencyMapper;
    private final CryptoMarketCacheService cryptoMarketCacheService;

    @Transactional
    public void synchronizeData(List<ExternalCryptoDto> externalData) {
        Set<String> existingApiIds = repository.findAllApiIds();

        List<CryptoCurrency> newCryptos = externalData.stream()
                .filter(dto -> !existingApiIds.contains(dto.getApiId()))
                .map(cryptoCurrencyMapper::toEntity)
                .toList();

        if(!newCryptos.isEmpty()) {
            repository.saveAll(newCryptos);

            log.info("Successfully saved {} new cryptocurrencies", newCryptos.size());
        }

        cryptoMarketCacheService.cacheDashboard(externalData);
        cryptoMarketCacheService.cachePrices(externalData);

        log.info("Synchronization finished");
    }
}
