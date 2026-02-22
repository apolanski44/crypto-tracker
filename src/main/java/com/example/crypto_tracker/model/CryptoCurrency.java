package com.example.crypto_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "crypto_currencies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoCurrency
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Column(name = "api_id", nullable = false)
    private String apiId;

    @Column(name = "current_price", precision = 18, scale = 8)
    private BigDecimal currentPrice;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
