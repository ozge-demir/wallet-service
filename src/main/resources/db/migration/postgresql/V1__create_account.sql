-- PostgreSQL sürümü. Oracle karşılığı: db/migration/oracle/V1__create_account.sql
-- Gün 0 görevi: iki dosyayı yan yana aç, 4 farkı bul.

CREATE TABLE account (
    id          BIGSERIAL       PRIMARY KEY,
    owner_name  VARCHAR(100)    NOT NULL,
    currency    CHAR(3)         NOT NULL,
    balance     NUMERIC(19, 2)  NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ     NOT NULL DEFAULT now(),
    CONSTRAINT account_balance_non_negative CHECK (balance >= 0)
);
