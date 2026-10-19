package com.ozge.wallet.account;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * API'nin döndüğü cevap. Entity'yi doğrudan dışarı açmıyoruz: tablo değişse bile API sözleşmesi bozulmasın.
 */
public record AccountResponse(Long id, String ownerName, String currency, BigDecimal balance, Instant createdAt) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getOwnerName(),
                account.getCurrency(),
                account.getBalance(),
                account.getCreatedAt());
    }
}
