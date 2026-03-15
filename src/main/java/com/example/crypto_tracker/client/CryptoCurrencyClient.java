package com.example.crypto_tracker.client;

import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;

import java.util.List;

public interface CryptoCurrencyClient {
    List<ExternalCryptoDto> fetchTopCurrencies();
}
