# Booking Service

Booking servis pokriva domen rezervacija i gostiju.

## Pokretanje

```powershell
.\mvnw.cmd spring-boot:run
```

Servis radi na portu `8081`.

## Z1


## Swagger i H2

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`
- H2 Console: `http://localhost:8081/h2-console`

H2 konekcija:

- JDBC URL: `jdbc:h2:file:./data/bookingdb;MODE=PostgreSQL;AUTO_SERVER=TRUE`
- korisnik: `sa`
- lozinka: prazno

## Glavni endpointi

- `POST /api/guests`
- `GET /api/guests`
- `GET /api/guests/{id}`
- `POST /api/bookings`
- `GET /api/bookings/{id}`
- `GET /api/bookings/search`
- `GET /api/bookings/revenue-summary`
- `PUT /api/bookings/{id}/status`
- `GET /api/bookings/{id}/price-preview`
- `POST /api/cancellation-policies`
- `GET /api/cancellation-policies`
- `GET /api/cancellation-policies/{id}`
- `GET /api/system/status`

## Model

Glavni entiteti:

- `Guest`
- `Booking`
- `CancellationPolicy`
- `AddOnItem`
- `BookingStatusLog`

Booking servis koristi sopstvenu bazu. Relacije unutar servisa modelovane su kao JPA veze i strani kljucevi.

## Seed podaci

Pri startu se izvrsavaju:

- `schema.sql`
- `import.sql`

Seed podaci ubacuju po jednog gosta, cancellation policy, booking, add-on item i booking status log.

## Z2 - slozeni upiti

Booking servis sadrzi dva slozena upita za demonstraciju cetvrte tacke zadatka 2.

1. `GET /api/bookings/search`

Ovaj endpoint koristi JPQL `JOIN` nad:

- `Booking`
- `Guest`
- `CancellationPolicy`

Podrzani filteri:

- `status`
- `guestEmail`
- `startFrom`
- `endTo`

Primer:

```text
GET /api/bookings/search?status=PENDING&guestEmail=ana&startFrom=2026-05-01&endTo=2026-05-31
```

2. `GET /api/bookings/revenue-summary`

Ovaj endpoint koristi agregaciju po statusu rezervacije i vraca:

- broj rezervacija po statusu
- zbir booking cena
- zbir add-on cena
- ukupan kombinovani prihod

## Globalna integracija

Booking servis izlaže interne DTO endpoint-e za `accommodation-service`:

- `GET /api/bookings/internal/units/{unitId}/summary`
- `GET /api/bookings/internal/units/{unitId}/occupancy?date=`

Za ove pozive validiraju se identifikaciona zaglavlja `X-Request-Id`, `X-Authenticated-User` i `X-User-Role`.
