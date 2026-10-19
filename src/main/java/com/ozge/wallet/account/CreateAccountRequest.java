package com.ozge.wallet.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

/**
 * İstek gövdesi. record: Java 16+ ile gelen, değişmez (immutable) veri taşıyıcı sınıf.
 * Getter, equals, hashCode ve toString otomatik üretilir.
 */
public record CreateAccountRequest(
        @NotBlank String ownerName,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency,
        @NotNull @DecimalMin("0.00") BigDecimal initialBalance
) {
}
