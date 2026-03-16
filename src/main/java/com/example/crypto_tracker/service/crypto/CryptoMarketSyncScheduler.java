package com.example.crypto_tracker.service.crypto;

import com.example.crypto_tracker.client.CryptoCurrencyClient;
import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "crypto.sync.enabled", havingValue = "true", matchIfMissing = true)
public class CryptoMarketSyncScheduler {
    private final CryptoCurrencyClient currencyClient;
    private final CryptoCurrencySynchronizer cryptoCurrencySynchronizer;

    public void synchronizeOnStartup() {
        log.info("Starting initial cryptocurrency synchronization");

        synchronize();
    }

    @Scheduled(
            fixedRateString = "${crypto.sync.fixed-rate-period}",
            initialDelayString = "${crypto.sync.fixed-rate-period-delay}"
    )
    public void synchronizePeriodically() {
        log.info("Starting scheduled cryptocurrency synchronization");

        synchronize();
    }

    private void synchronize() {
        List<ExternalCryptoDto> externalData = currencyClient.fetchTopCurrencies();

        if (externalData == null || externalData.isEmpty()) {
            log.warn("External API returned no currencies");

            return;
        }

        cryptoCurrencySynchronizer.synchronizeData(externalData);
    }
}
