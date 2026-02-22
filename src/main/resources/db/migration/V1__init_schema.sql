CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at DATETIME NOT NULL
);

CREATE TABLE crypto_currencies (
    id VARCHAR(36) PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    api_id VARCHAR(255) NOT NULL,
    current_price DECIMAL(18, 8),
    last_updated_at DATETIME
);

CREATE TABLE transactions (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    crypto_currency_id VARCHAR(36) NOT NULL,
    amount DECIMAL(18, 8) NOT NULL,
    purchase_price DECIMAL(18, 2) NOT NULL,
    type VARCHAR(10) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_crypto FOREIGN KEY (crypto_currency_id) REFERENCES crypto_currencies(id)
);