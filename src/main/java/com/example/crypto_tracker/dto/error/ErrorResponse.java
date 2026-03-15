package com.example.crypto_tracker.dto.error;

import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String message;

    @Builder.Default
    private final String timestamp = ZonedDateTime.now(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_INSTANT);
}
