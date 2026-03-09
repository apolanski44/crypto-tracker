package com.example.crypto_tracker.repository;

import com.example.crypto_tracker.model.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, UUID> {
}
