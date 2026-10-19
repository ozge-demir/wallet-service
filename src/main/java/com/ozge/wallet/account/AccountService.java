package com.ozge.wallet.account;

import com.ozge.wallet.common.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    // Constructor injection: Spring AccountRepository'yi buraya kendisi verir (DI).
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account open(CreateAccountRequest request) {
        var account = new Account(request.ownerName(), request.currency(), request.initialBalance());
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account get(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }
}
