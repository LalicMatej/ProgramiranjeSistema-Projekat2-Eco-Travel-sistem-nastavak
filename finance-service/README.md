# Finance Service

Finance servis pokriva fakture, poreske stope, metode placanja i transakcije.

## Pokretanje

```powershell
.\mvnw.cmd spring-boot:run
```

Servis radi na portu `8082`.

## Z1


## Swagger i H2

- Swagger UI: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/api-docs`
- H2 Console: `http://localhost:8082/h2-console`

H2 konekcija:

- JDBC URL: `jdbc:h2:file:./data/financedb;MODE=PostgreSQL;AUTO_SERVER=TRUE`
- korisnik: `sa`
- lozinka: prazno

## Glavni endpointi

- `POST /api/tax-rates`
- `GET /api/tax-rates`
- `GET /api/tax-rates/{id}`
- `POST /api/payment-methods`
- `GET /api/payment-methods`
- `GET /api/payment-methods/{id}`
- `POST /api/invoices`
- `GET /api/invoices/{id}`
- `GET /api/invoices/{id}/total`
- `POST /api/invoices/{id}/transactions`
- `GET /api/system/status`

## Model

Glavni entiteti:

- `TaxRate`
- `PaymentMethod`
- `Invoice`
- `Transaction`

Finance servis koristi sopstvenu bazu. Veza prema booking domenu modelovana je samo preko `bookingId`, bez JPA relacije ka booking servisu.

## Poslovna logika

- ukupni iznos fakture racuna se na osnovu `subtotal` i procenata iz `TaxRate`
- transakcija ne moze da preplati fakturu
- neaktivan payment method ne moze da se koristi
- puna uplata automatski prebacuje invoice u status `PAID`

## Seed podaci

Pri startu se izvrsavaju:

- `schema.sql`
- `import.sql`

Seed podaci ubacuju poreske stope, payment method, jednu fakturu i jednu transakciju.

## Z2

### Interna komunikacija preko OpenFeign

`finance-service` poziva `booking-service` preko Feign klijenta `BookingClient`.

Feign klijent koristi tri metode:

- `GET /api/bookings/{id}`
- `GET /api/bookings/{id}/price-preview`
- `GET /api/bookings/{id}/summary`

Razmena podataka se radi preko DTO objekata iz `dto.integration` paketa. `finance-service` ne koristi entitete iz `booking-service`, vec samo lokalne DTO kopije remote odgovora.

Primer toka:

1. Korisnik pozove `POST /api/invoices`.
2. `InvoiceService` trazi podatke o rezervaciji.
3. `BookingClient` preko Feign-a poziva `booking-service`.
4. Dobijeni booking podaci se koriste za validaciju i obracun invoice-a.

### Arhitektonski obrasci

Implementirana su dva pattern-a:

- `Facade`
- `Factory`

`BookingClientFacade` predstavlja `Facade` pattern. On sakriva detalje komunikacije sa `booking-service` od `InvoiceService`.

Bez facade-a, `InvoiceService` bi morao direktno da zna za:

- Feign klijenta
- retry i circuit breaker anotacije
- fallback metode
- obradu remote gresaka
- standardizovanje rezultata poziva

Sa facade-om, `InvoiceService` koristi jednostavne metode:

- `getBooking`
- `getBookingPricePreview`
- `getBookingSummary`

`BookingClientResultFactory` predstavlja `Factory Method` pattern. On centralizovano pravi standardizovane rezultate Feign poziva:

- uspesan rezultat
- booking nije pronadjen
- booking servis nije dostupan
- nevalidan remote odgovor

### Otpornost: Retry, Circuit Breaker i fallback

Svi Feign pozivi ka `booking-service` su zasticeni preko Resilience4j mehanizama.

Konfiguracija koristi:

- `bookingServiceRetry`
- `bookingServiceCircuitBreaker`

Retry konfiguracija:

- maksimalno 3 pokusaja
- pocetno cekanje 500ms
- ukljucen exponential backoff

Circuit breaker konfiguracija:

- count-based sliding window
- failure threshold 50%
- minimum 3 poziva pre procene greske
- open state traje 20s

Fallback metode se nalaze u `BookingClientFacade`. Kada `booking-service` nije dostupan ili je circuit breaker otvoren, facade ne pusta nekontrolisanu server gresku, vec vraca standardizovan rezultat koji se dalje mapira u kontrolisan API odgovor.

### Poslovna logika i slozeni upiti

Zahtev za minimalno dva slozena upita pokriven je kroz oba servisa.

U `booking-service` postoje:

- `GET /api/bookings/search`
- `GET /api/bookings/revenue-summary`

Prvi endpoint koristi `JOIN` nad `Booking`, `Guest` i `CancellationPolicy`, uz filtere po statusu, email-u gosta i datumima.

Drugi endpoint koristi agregaciju po statusu rezervacije i kombinuje booking cenu sa add-on cenama.

U `finance-service` dodatno postoji agregacioni upit:

- `TransactionRepository.sumAmountsByInvoiceId`

Ovaj upit racuna ukupno placeni iznos za jednu fakturu preko `SUM` agregacije nad transakcijama.

## Monitoring demo

Finance servis je centralno mesto za demonstraciju otpornosti, jer preko Feign klijenta poziva `booking-service`.

Actuator endpointi:

- Health: `http://localhost:8082/actuator/health`
- Metrics: `http://localhost:8082/actuator/metrics`
- Circuit breakers: `http://localhost:8082/actuator/circuitbreakers`
- Circuit breaker events: `http://localhost:8082/actuator/circuitbreakerevents`
- Retries: `http://localhost:8082/actuator/retries`
- Retry events: `http://localhost:8082/actuator/retryevents`
