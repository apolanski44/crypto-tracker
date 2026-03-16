package com.example.crypto_tracker.initializer;

import com.example.crypto_tracker.service.crypto.CryptoMarketSyncScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "crypto.sync.enabled", havingValue = "true", matchIfMissing = true)
public class CryptoStartupInitializer implements ApplicationRunner {
    private final CryptoMarketSyncScheduler cryptoMarketSyncScheduler;

    @Override
    public void run(ApplicationArguments args) {
        cryptoMarketSyncScheduler.synchronizeOnStartup();
    }
}
