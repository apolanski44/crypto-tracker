package com.example.crypto_tracker.service.crypto;

import com.example.crypto_tracker.model.CryptoCurrency;
import com.example.crypto_tracker.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyService {
    private final CryptoCurrencyRepository repository;
    private final CryptoCurrencyMapper mapper;

    @Cacheable(value = "currencies")
    public List<CryptoCurrency> getAllCurrencies() {
        return repository.findAll();
    }
}
