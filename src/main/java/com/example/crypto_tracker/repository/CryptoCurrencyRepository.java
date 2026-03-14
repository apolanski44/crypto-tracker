package com.example.crypto_tracker.repository;

import com.example.crypto_tracker.model.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, UUID> {
    @Query("select c.apiId from CryptoCurrency c")
    Set<String> findAllApiIds();
}
