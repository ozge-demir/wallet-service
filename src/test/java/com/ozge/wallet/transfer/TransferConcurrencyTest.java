package com.ozge.wallet.transfer;

import com.ozge.wallet.account.AccountService;
import com.ozge.wallet.account.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Gün 1: bu testler başta KIRMIZI. TransferService'i yazınca yeşile dönecek.
 *
 * 100 transfer aynı anda, her biri ayrı bir virtual thread'de çalışıyor.
 * Virtual thread (Java 21): JVM'in yönettiği çok hafif thread; binlercesini açmak ucuz.
 */
@SpringBootTest
class TransferConcurrencyTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransferService transferService;

    @Test
    void concurrentWithdrawalsDoNotLoseMoney() throws Exception {
        var a = accountService.open(new CreateAccountRequest("A", "EUR", new BigDecimal("1000.00")));
        var b = accountService.open(new CreateAccountRequest("B", "EUR", new BigDecimal("0.00")));

        // 100 kez A'dan B'ye 10 EUR: sonunda A = 0, B = 1000 olmalı.
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tasks.add(() -> {
                transferService.transfer(a.getId(), b.getId(), new BigDecimal("10.00"));
                return null;
            });
        }

        var errors = runConcurrently(tasks);

        var finalA = accountService.get(a.getId()).getBalance();
        var finalB = accountService.get(b.getId()).getBalance();
        System.out.printf("%n>>> A son bakiye: %s, B son bakiye: %s, hata sayısı: %d%n%n", finalA, finalB, errors.size());

        assertThat(errors).as("hiçbir transfer hata vermemeli").isEmpty();
        assertThat(finalA).isEqualByComparingTo("0.00");
        assertThat(finalB).isEqualByComparingTo("1000.00");
    }

    @Test
    void transfersInBothDirectionsDoNotDeadlock() throws Exception {
        var a = accountService.open(new CreateAccountRequest("A", "EUR", new BigDecimal("1000.00")));
        var b = accountService.open(new CreateAccountRequest("B", "EUR", new BigDecimal("1000.00")));

        // 50 transfer A->B, 50 transfer B->A, aynı anda. Sonunda ikisi de 1000 olmalı.
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            tasks.add(() -> {
                transferService.transfer(a.getId(), b.getId(), new BigDecimal("10.00"));
                return null;
            });
            tasks.add(() -> {
                transferService.transfer(b.getId(), a.getId(), new BigDecimal("10.00"));
                return null;
            });
        }

        var errors = runConcurrently(tasks);
        errors.stream().limit(3).forEach(e -> System.out.println(">>> Hata: " + e));

        var finalA = accountService.get(a.getId()).getBalance();
        var finalB = accountService.get(b.getId()).getBalance();

        assertThat(errors).as("deadlock ya da başka hata olmamalı").isEmpty();
        assertThat(finalA).isEqualByComparingTo("1000.00");
        assertThat(finalB).isEqualByComparingTo("1000.00");
    }

    private List<Throwable> runConcurrently(List<Callable<Void>> tasks) throws InterruptedException {
        List<Throwable> errors = new ArrayList<>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (var future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    errors.add(e.getCause());
                }
            }
        }
        return errors;
    }
}
