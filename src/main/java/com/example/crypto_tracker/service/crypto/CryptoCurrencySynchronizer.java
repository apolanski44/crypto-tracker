package com.example.crypto_tracker.service.crypto;

import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import com.example.crypto_tracker.model.CryptoCurrency;
import com.example.crypto_tracker.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoCurrencySynchronizer {
    private final CryptoCurrencyRepository repository;
    private final CryptoCurrencyMapper cryptoCurrencyMapper;

    @Transactional
    @CacheEvict(value = "currencies", allEntries = true)
    public void synchronizeData(List<ExternalCryptoDto> externalData) {
        List<String> existingApiIds = repository.findAll().stream()
                .map(CryptoCurrency::getApiId)
                .toList();

        List<CryptoCurrency> newCryptos = externalData.stream()
                .filter(dto -> !existingApiIds.contains(dto.getApiId()))
                .map(cryptoCurrencyMapper::toEntity)
                .toList();

        if(!newCryptos.isEmpty()) {
            repository.saveAll(newCryptos);

            log.info("Successfully saved {} new cryptocurrencies", newCryptos.size());
        } else {
            log.info("No new cryptocurrencies to save");
        }
    }
}
