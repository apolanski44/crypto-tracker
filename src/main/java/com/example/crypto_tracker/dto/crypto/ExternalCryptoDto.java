package com.example.crypto_tracker.dto.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalCryptoDto {
    @JsonProperty("id")
    private String apiId;

    private String symbol;

    private String name;

    private String image;

    @JsonProperty("current_price")
    private BigDecimal currentPrice;
}
