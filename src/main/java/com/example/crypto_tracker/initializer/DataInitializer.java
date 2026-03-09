package com.example.crypto_tracker.initializer;

import com.example.crypto_tracker.client.CoinGeckoClient;
import com.example.crypto_tracker.client.CryptoCurrencyClient;
import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import com.example.crypto_tracker.service.crypto.CryptoCurrencySynchronizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final CryptoCurrencyClient client;
    private final CryptoCurrencySynchronizer synchronizer;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Starting crypto data initialization");

        try {
            List<ExternalCryptoDto> externalData = client.fetchTopCurrencies();

            synchronizer.synchronizeData(externalData);

            log.info("Data initialization finished successfully");
        } catch (Exception e) {
            log.error("Failed to initialize crypto data: {}", e.getMessage());
        }
    }

}
