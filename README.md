# Cüzdan Servisi (wallet-service)

Java 21, Spring Boot, Hibernate/JPA, PostgreSQL ve Flyway ile yazılmış bir cüzdan/para transferi servisi.
Her gün bir özellik ekleniyor; konular (transaction, kilit, N+1, Kafka, Redis, güvenlik) o özelliğin ihtiyacından çıkıyor.

## Çalıştırma

```bash
docker compose up -d postgres     # PostgreSQL 16, localhost:5432
./mvnw test                       # ya da: mvn test
./mvnw spring-boot:run            # ya da: mvn spring-boot:run
```

## Hesap açma

```bash
curl -i -X POST localhost:8080/api/accounts \
  -H 'Content-Type: application/json' \
  -d '{"ownerName":"Özge","currency":"EUR","initialBalance":1000}'

curl -i localhost:8080/api/accounts/1
```

## Transfer (Gün 1'den sonra çalışır)

```bash
curl -i -X POST localhost:8080/api/transfers \
  -H 'Content-Type: application/json' \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":10}'
```

## Yapı

```
account/    Account (entity), AccountRepository, AccountService, AccountController
transfer/   TransferService (Gün 1: kilitler), TransferController
common/     Exception'lar ve hata cevapları (ProblemDetail)
db/migration/postgresql   Çalışan Flyway migration'ları
db/migration/oracle       Aynı şemanın Oracle karşılığı (sadece karşılaştırma için)
```
