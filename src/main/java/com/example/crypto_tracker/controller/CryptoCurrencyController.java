package com.example.crypto_tracker.controller;

import com.example.crypto_tracker.dto.crypto.DashboardCurrencyDto;
import com.example.crypto_tracker.service.crypto.dashboard.CryptoDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CryptoCurrencyController {
    private final CryptoDashboardService cryptoDashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardCurrencyDto>> getDashboardCurrencies() {
        List<DashboardCurrencyDto> dashboardCurrencies = cryptoDashboardService.getDashboardCurrencies();

        return ResponseEntity.ok(dashboardCurrencies);
    }
}
