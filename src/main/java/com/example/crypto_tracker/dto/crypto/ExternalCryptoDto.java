package com.example.crypto_tracker.dto.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalCryptoDto {
    @JsonProperty("id")
    private String apiId;

    private String symbol;

    private String name;
}
