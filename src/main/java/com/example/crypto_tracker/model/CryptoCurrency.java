package com.example.crypto_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Column(name = "api_id", nullable = false)
    private String apiId;

    @Column(name = "current_price", precision = 18, scale = 8)
    private BigDecimal currentPrice;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
