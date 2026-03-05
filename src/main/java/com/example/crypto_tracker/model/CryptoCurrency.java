package com.example.crypto_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
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

    @Column(length = 10, unique = true, nullable = false)
    private String symbol;

    @Column(name = "api_id", nullable = false, unique = true)
    private String apiId;

    @Column(name = "name", nullable = false)
    private  String name;
}
