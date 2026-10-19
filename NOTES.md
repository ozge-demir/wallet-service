# Öğrenme Notları — wallet-service

## Gün 0: Proje iskeleti ve hesap API'si

### Hibernate SQL'i
- Kodda SQL yazmıyorum; `repository.save()` çağrısını Hibernate SQL'e çeviriyor.
- Log'daki `values (?, ?, ?, ?)`: değerler SQL metnine gömülmüyor, ayrı parametre
  olarak gidiyor (PreparedStatement). Bu SQL injection'ı engelliyor.
- INSERT'te `id` sütunu yok, çünkü id'yi PostgreSQL üretiyor (bigserial → sequence).

### PostgreSQL ve Oracle farkları
| | PostgreSQL | Oracle |
|---|---|---|
| Sayı | NUMERIC | NUMBER |
| Metin | VARCHAR | VARCHAR2 |
| Zaman | TIMESTAMPTZ | TIMESTAMP WITH TIME ZONE |
| Şimdiki zaman | now() | SYSTIMESTAMP |
| Otomatik id | BIGSERIAL | NUMBER ... GENERATED AS IDENTITY |

En çok şaşırdığım iki fark:
1. Oracle'da neredeyse her sayinin tipi NUMBER, PostgreSQL ise ihtiyaca gore integer ve bigint veya numeric kullaniyor
2. Oracle bos metni NULL sayar (varchar2); PostgreSQL ise bos metni null saymaz, bos metin bos metin; NULL, NULL'dur

### Zaman ve saat dilimi
- Veritabanı zamanı UTC olarak saklıyor (`+00`), uygulama log'u İstanbul saatiyle
  yazıyor (`+03`). Aynı an, farklı gösterim.

### Maven yaşam döngüsü
- `clean → compile → test → package → install`. `install` dediğimde testler de çalışıyor.

### Test izolasyonu
- Testler geliştirme veritabanıma yazıp kayıtlarını silmedi; tabloda fazladan
  "Özge" kayıtları oluştu. Çözüm: testlere ayrı, geçici bir veritabanı (Testcontainers).

### Dependency Injection
- Sınıf ihtiyaç duyduğu nesneyi `new` ile oluşturmaz, dışarıdan alır.
  Bu nesneleri Spring IoC container yönetir.
- Örnek: AccountController, AccountService'i constructor'ından alıyor.

### Neden constructor injection?
- Bağımlılıklar `final` olur.
- Testte Spring ya da reflection olmadan constructor'a mock verebilirim.
  Field injection'da alan private olduğu için `new` ile oluşturunca null kalır.
- Eksik bağımlılık çalışma sırasında NullPointerException yerine
  uygulama açılırken hata verir.
- Çok fazla bağımlılık constructor'da göze batar (Single Responsibility uyarısı).