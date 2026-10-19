package com.ozge.wallet.common;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Hesap bulunamadı: " + id);
    }
}
