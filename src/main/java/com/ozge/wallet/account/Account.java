package com.ozge.wallet.account;

import com.ozge.wallet.common.InsufficientFundsException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Bir cüzdan hesabı. Para her zaman BigDecimal: double ile 0.1 + 0.2 != 0.3 olur.
 */
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // id'yi veritabanı üretir (BIGSERIAL / IDENTITY)
    private Long id;

    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // TODO (Gün 1 bonus): optimistic lock için buraya @Version alanı eklenecek
    //  (önce V2 migration ile tabloya "version BIGINT NOT NULL DEFAULT 0" kolonu ekle).

    protected Account() {
        // JPA için gerekli boş constructor
    }

    public Account(String ownerName, String currency, BigDecimal initialBalance) {
        this.ownerName = ownerName;
        this.currency = currency;
        this.balance = initialBalance;
        this.createdAt = Instant.now();
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public Instant getCreatedAt() { return createdAt; }
}
