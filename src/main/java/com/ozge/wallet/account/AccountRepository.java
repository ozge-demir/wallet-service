package com.ozge.wallet.account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data bu arayüzün gövdesini çalışma anında kendisi üretir (findById, save, ...).
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    // TODO (Gün 1, adım 2): satırı okurken kilitleyen bir metot ekle.
    //  İpucu: @Lock(LockModeType.PESSIMISTIC_WRITE) + @Query("select a from Account a where a.id = :id")
    //  Optional<Account> findByIdForUpdate(@Param("id") Long id);
}
