package com.ozge.wallet.transfer;

import com.ozge.wallet.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * TODO (Gün 1): para transferini sen yazacaksın. TransferConcurrencyTest şu an bu yüzden kırmızı.
     *
     * Adım 1 - Saf hali:
     *   - Metoda @Transactional ekle.
     *   - İki hesabı findById ile oku (yoksa AccountNotFoundException fırlat).
     *   - from.withdraw(amount), to.deposit(amount).
     *   - save çağırmana gerek yok: transaction içindeki entity'leri Hibernate commit anında kendisi günceller
     *     (buna "dirty checking" denir).
     *   - Testi çalıştır. Kırmızı kalacak. A hesabının son bakiyesi kaç çıktı? Not al.
     *
     * Adım 2 - Kilit:
     *   - AccountRepository'ye findByIdForUpdate ekle ve findById yerine onu kullan.
     *   - Konsol loglarında "for update" ifadesini bul.
     *
     * Adım 3 - Deadlock:
     *   - A->B ve B->A aynı anda çalışırsa ne olur? Kilitleri her zaman küçük id'den başlayarak al.
     *   - İki test de yeşile dönmeli.
     */
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        throw new UnsupportedOperationException("TODO (Gün 1): transfer henüz yazılmadı");
    }
}
