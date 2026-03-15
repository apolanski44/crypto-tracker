package com.example.crypto_tracker.service.crypto;

import com.example.crypto_tracker.dto.crypto.ExternalCryptoDto;
import com.example.crypto_tracker.model.CryptoCurrency;
import org.springframework.stereotype.Component;

@Component
public class CryptoCurrencyMapper {
    public CryptoCurrency toEntity(ExternalCryptoDto dto) {
        if (dto == null) {
            return null;
        }

        return CryptoCurrency.builder()
                .symbol(dto.getSymbol())
                .apiId(dto.getApiId())
                .name(dto.getName())
                .image(dto.getImage())
                .build();
    }
}
