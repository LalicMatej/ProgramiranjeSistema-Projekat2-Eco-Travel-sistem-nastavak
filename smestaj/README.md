# Smestaj Mikroservis

## Tehnicke specifikacije

| Stavka | Vrednost |
|---|---|
| Java verzija | 22 |
| Spring Boot | 3.3.5 |
| Spring Cloud | 2023.0.3 |
| Baza podataka | H2 (in-memory) |
| Build alat | Maven |
| **Port** | **8081** |

### Kljucne zavisnosti

- `spring-boot-starter-web` — REST API
- `spring-boot-starter-data-jpa` — JPA/Hibernate ORM
- `spring-boot-starter-validation` — validacija ulaznih podataka
- `spring-boot-starter-actuator` — health i monitoring endpointi
- `spring-cloud-starter-config` — podrska za Config Server
- `spring-cloud-starter-openfeign` — meduservisna komunikacija (pozivi ka Odrzavanje servisu)
- `spring-cloud-starter-circuitbreaker-resilience4j` — circuit breaker
- `resilience4j-spring-boot3` — rate limiter
- `springdoc-openapi-starter-webmvc-ui` — Swagger/OpenAPI dokumentacija
- `micrometer-tracing-bridge-brave` — distribuirano pracenje (tracing)
- `lombok` — redukcija boilerplate koda
- `h2` — in-memory relaciona baza podataka

---

## Opis poslovne logike

Smestaj mikroservis upravlja smestajnim jedinicama (kolibe, apartmani, vile) i njihovim rezervacijama. Servis pokriva sledece domene:

- **Unit (Smestajna jedinica)** — CRUD operacije nad smestajnim jedinicama. Svaka jedinica ima naziv, tip (`KOLIBA`, `APARTMAN`, `VILA`), osnovnu cenu po nocenju, adresu i listu sadrzaja (facilities). Podrska za pretragu po imenu, gradu, tipu i cenovnom opsegu.
- **AvailabilityCalendar (Kalendar dostupnosti)** — upravljanje rezervacijama i blokadama termina. Validira da novi termin ne preklapa sa postojecim.
- **PriceTier (Cenovni nivoi)** — sezonske varijacije cena (npr. letnji/zimski multiplikatori). Cena rezervacije se racuna kao: (osnovna cena + troškovi sadrzaja) × multiplikator × broj nocenja.
- **Address (Adresa)** i **Facility (Sadrzaj)** — pomocni entiteti za opis smestajnih jedinica.

Servis komunicira sa **Odrzavanje mikroservisom** putem Feign klijenta (`http://localhost:8082`) kako bi dobio informacije o radnim nalozima za smestajne jedinice.

### Obrasci otpornosti

| Obrazac | Naziv instance | Konfiguracija |
|---|---|---|
| Circuit Breaker | `odrzavanjeService` | Prozor: 10, min. poziva: 3, prag gresaka: 50%, cekanje: 10s, poluotvoreno: 3 poziva |
| Retry | `odrzavanjeService` | Maks. pokusaji: 3, cekanje: 500ms |
| Time Limiter | `odrzavanjeService` | Timeout: 5s |
| Rate Limiter | `createUnit` | 3 zahteva / 60s |
| Rate Limiter | `createReservation` | 10 zahteva / 60s |

---

## Implementirani endpointi

### UnitController — `/api/units`

| Metoda | Putanja | Opis |
|---|---|---|
| POST | `/api/units/add` | Kreira novu smestajnu jedinicu (rate limiter: 3/60s) |
| GET | `/api/units/all` | Vraca sve smestajne jedinice |
| GET | `/api/units/{id}` | Vraca smestajnu jedinicu po ID-ju |
| GET | `/api/units/{id}/exists` | Proverava da li smestajna jedinica postoji (vraca Boolean) |
| DELETE | `/api/units/{id}` | Brise smestajnu jedinicu po ID-ju |
| GET | `/api/units/search?name={name}` | Pretrazuje jedinice po nazivu (min. 2 karaktera) |
| GET | `/api/units/city?city={city}` | Pretrazuje jedinice po gradu (min. 2 karaktera) |
| GET | `/api/units/findByParameters?type={type}&minPrice={min}&maxPrice={max}` | Filtrira jedinice po tipu i cenovnom opsegu (svi parametri opcioni) |
| GET | `/api/units/{unitId}/calculate-price?startDate={date}&endDate={date}` | Racuna ukupnu cenu za zadati period (osnovna cena + sadrzaji + sezonski multiplikator) |
| GET | `/api/units/{unitId}/with-work-orders` | Vraca jedinicu zajedno sa radnim nalozima iz Odrzavanje servisa (circuit breaker) |
| GET | `/api/units/unitsWithsWorksInProgress` | Vraca jedinice koje imaju aktivne radne naloge (circuit breaker) |
| GET | `/api/units/cityStatistics` | Vraca agregirane statistike po gradovima |

### AvailabilityCalendarController — `/api/reserve`

| Metoda | Putanja | Opis |
|---|---|---|
| POST | `/api/reserve/add` | Dodaje rezervaciju ili blokadu termina — validira nepostojanje preklapanja (rate limiter: 10/60s) |

### PriceTierController — `/api/priceTiers`

| Metoda | Putanja | Opis |
|---|---|---|
| POST | `/api/priceTiers/add` | Dodaje sezonski cenovni nivo za smestajnu jedinicu — validira nepostojanje preklapanja i max. trajanje od 365 dana |

---

## Mrezni port

Servis operise na portu **`8081`**.

Konfiguracija se ucitava sa Config Servera pri pokretanju. Relevantna datoteka:
`ConfigService/src/main/resources/config/smestaj-mikroservis.properties`

| Resurs | URL |
|---|---|
| Swagger UI | http://localhost:8081/swagger-ui.html |
| H2 konzola | http://localhost:8081/h2-console |
| Health check | http://localhost:8081/actuator/health |
| Circuit breakers | http://localhost:8081/actuator/circuitbreakers |
| Rate limiters | http://localhost:8081/actuator/ratelimiters |

---

## Lokalno pokretanje i testiranje

### Preduslovi

- JDK 22+
- Maven 3.9+
- Pokrenuti **Config Server** (port `8888`) pre pokretanja ovog servisa

### Redosled pokretanja

Config Server mora biti pokrenut prvi jer ostali servisi ucitavaju konfiguraciju sa njega pri startu.

```bash
# 1. Pokrenuti Config Server
cd ConfigService
mvn spring-boot:run

# 2. Pokrenuti Smestaj servis
cd smestaj
mvn spring-boot:run
```

Alternativno iz IDE (IntelliJ IDEA / Eclipse):
1. Importovati projekat kao Maven projekat
2. Pokrenuti `Main.java` iz `ConfigService` modula
3. Pokrenuti `UnitApplication.java` iz `smestaj` modula

### Provera uspesnog pokretanja

```bash
curl http://localhost:8081/actuator/health
```

Ocekivani odgovor: `{"status":"UP"}`

### Primeri testiranja endpointa

#### Dobaviti sve smestajne jedinice
```bash
curl -X GET http://localhost:8081/api/units/all
```

#### Kreirati novu smestajnu jedinicu
```bash
curl -X POST http://localhost:8081/api/units/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Apartman Test",
    "unitType": "APARTMAN",
    "basePricePerNight": 120.0,
    "address": {
      "city": "Beograd",
      "zipCode": "11000",
      "streetAddress": "Knez Mihailova 1"
    }
  }'
```

#### Pretraga po gradu
```bash
curl -X GET "http://localhost:8081/api/units/city?city=Zlatibor"
```

#### Filtriranje po tipu i ceni
```bash
curl -X GET "http://localhost:8081/api/units/findByParameters?type=VILA&minPrice=150&maxPrice=300"
```

#### Izracunavanje cene za period
```bash
curl -X GET "http://localhost:8081/api/units/1/calculate-price?startDate=2026-07-01&endDate=2026-07-10"
```

#### Kreiranje rezervacije
```bash
curl -X POST http://localhost:8081/api/reserve/add \
  -H "Content-Type: application/json" \
  -d '{
    "unitId": 1,
    "startDate": "2026-08-01",
    "endDate": "2026-08-07",
    "reason": "Rezervacija"
  }'
```

#### Dodavanje sezonskog cenovnog nivoa
```bash
curl -X POST http://localhost:8081/api/priceTiers/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Letnja sezona",
    "multiplier": 1.5,
    "startDate": "2026-06-01",
    "endDate": "2026-08-31",
    "unitId": 1
  }'
```

### Baza podataka

Baza se automatski inicijalizuje sa test podacima pri svakom pokretanju (`data.sql`):
- 4 smestajne jedinice (Brvnara Bor, Apartman Sunny, Vila Zlatibor, Planinska kuca)
- 4 adrese (Novi Sad, Beograd, Zlatibor, Kopaonik)
- 8 sadrzaja (Bazen, WiFi, Sauna, Džakuzi, Klima, Parking, Spa centar, Teretana)
- Sezonski cenovni nivoi za letnji i zimski period
- Rezervacije i blokirani termini

Pristup H2 konzoli: http://localhost:8081/h2-console

| Polje | Vrednost |
|---|---|
| JDBC URL | `jdbc:h2:mem:smestajservis` |
| Korisnicko ime | `smestajservis` |
| Lozinka | `smestajservis` |

### Pokretanje testova

```bash
cd smestaj
mvn test
```
