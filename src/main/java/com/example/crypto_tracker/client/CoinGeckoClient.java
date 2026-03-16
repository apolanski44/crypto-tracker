package com.example.crypto_tracker.client;

import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@ConditionalOnProperty(name = "application.client.enabled", havingValue = "true", matchIfMissing = true)
public class CoinGeckoClient implements CryptoCurrencyClient {
    private final RestClient restClient;

    public CoinGeckoClient(
            @Value("${application.coingecko.api-key}") String apiKey,
            @Value("${application.coingecko.base-url}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-cg-demo-api-key", apiKey)
                .build();
    }

    public List<ExternalCryptoDto> fetchTopCurrencies() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/coins/markets")
                        .queryParam("vs_currency", "usd")
                        .queryParam("order", "market_cap_desc")
                        .queryParam("per_page", 100)
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ExternalCryptoDto>>() {});
    }

}
