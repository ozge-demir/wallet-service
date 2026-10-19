package com.ozge.wallet.common;

import java.math.BigDecimal;

/**
 * Unchecked (RuntimeException): @Transactional varsayılan olarak sadece unchecked exception'da rollback yapar.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(Long accountId, BigDecimal balance, BigDecimal requested) {
        super("Yetersiz bakiye. Hesap " + accountId + ", bakiye " + balance + ", istenen " + requested);
    }
}
