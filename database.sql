-- PriceRadar database setup
-- Creates the database and the four tables required by the application.
-- This script does not insert sample products or delete existing data.

CREATE DATABASE IF NOT EXISTS priceradar_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE priceradar_db;

CREATE TABLE IF NOT EXISTS products (
    product_id INT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (product_id),
    UNIQUE KEY product_name (product_name)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS sellers (
    seller_id INT NOT NULL AUTO_INCREMENT,
    product_id INT NOT NULL,
    seller_name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    delivery_charge DOUBLE NOT NULL,
    delivery_days INT NOT NULL,
    PRIMARY KEY (seller_id),
    KEY product_id (product_id),
    CONSTRAINT sellers_ibfk_1
        FOREIGN KEY (product_id) REFERENCES products (product_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS price_history (
    history_id INT NOT NULL AUTO_INCREMENT,
    seller_id INT NOT NULL,
    price DOUBLE NOT NULL,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (history_id),
    KEY seller_id (seller_id),
    CONSTRAINT price_history_ibfk_1
        FOREIGN KEY (seller_id) REFERENCES sellers (seller_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS target_alerts (
    alert_id INT NOT NULL AUTO_INCREMENT,
    product_id INT NOT NULL,
    target_price DOUBLE NOT NULL,
    PRIMARY KEY (alert_id),
    KEY product_id (product_id),
    CONSTRAINT target_alerts_ibfk_1
        FOREIGN KEY (product_id) REFERENCES products (product_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
