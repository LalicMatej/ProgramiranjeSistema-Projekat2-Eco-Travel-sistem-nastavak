# Odrzavanje Mikroservis

## Tehnicke specifikacije

| Stavka | Vrednost |
|---|---|
| Java verzija | 17 |
| Spring Boot | 3.3.5 |
| Spring Cloud | 2023.0.3 |
| Baza podataka | H2 (in-memory) |
| Build alat | Maven |
| **Port** | **8082** |

### Kljucne zavisnosti

- `spring-boot-starter-web` — REST API
- `spring-boot-starter-data-jpa` — JPA/Hibernate ORM
- `spring-boot-starter-validation` — validacija ulaznih podataka
- `spring-boot-starter-actuator` — health i monitoring endpointi
- `spring-cloud-starter-config` — podrska za Config Server
- `spring-cloud-starter-openfeign` — meduservisna komunikacija (pozivi ka Smestaj servisu)
- `spring-cloud-starter-circuitbreaker-resilience4j` — circuit breaker
- `resilience4j-spring-boot3` — rate limiter
- `springdoc-openapi-starter-webmvc-ui` — Swagger/OpenAPI dokumentacija
- `micrometer-tracing-bridge-brave` — distribuirano pracenje (tracing)
- `lombok` — redukcija boilerplate koda
- `h2` — in-memory relaciona baza podataka

---

## Opis poslovne logike

Odrzavanje mikroservis upravlja planiranjem i pracenjem odrzavanja smestajnih jedinica. Servis pokriva sledece domene:

- **WorkOrder (Radni nalog)** — centralni entitet servisa. Radni nalog se kreira za odredjenu smestajnu jedinicu, dodeljuje se radniku i vezuje za tip zadatka. Prati stanje kroz tri faze: `PENDING` → `IN_PROGRESS` → `COMPLETED`. Kada se nalog zavrsi, automatski se kreira log unos (Observer obrazac).
- **Worker (Radnik)** — radnici sa specijalizacijama (npr. `elektricar`, `higijenicar`). Podrska za pretragu po specijalizaciji i statistike po tipu. Validacija dostupnosti radnika pri zakazivanju (vremenski bafer od 1h).
- **MaintenanceTask (Tip zadatka odrzavanja)** — katalog tipova zadataka sa procenjenim trajanjem (npr. "Popravka klime" — 2h, "Generalno čišćenje" — 3h).
- **MaintenanceLog (Dnevnik odrzavanja)** — automatski se popunjava kada se radni nalog oznaci kao zavrsen. Sadrzi opis zadatka, ime radnika, vreme zavrsetka i napomene.

Servis komunicira sa **Smestaj mikroservisom** putem Feign klijenta (`http://localhost:8081`) kako bi validirao postojanje smestajnih jedinica pri kreiranju radnih naloga.

### Implementirani obrasci

- **Observer** — `WorkOrderEventPublisher` / `WorkOrderObserver`: po zavrsetku radnog naloga automatski se kreira `MaintenanceLog` unos.
- **Circuit Breaker + Retry** — zastita poziva ka Smestaj servisu.

### Obrasci otpornosti

| Obrazac | Naziv instance | Konfiguracija |
|---|---|---|
| Circuit Breaker | `smestajService` | Prozor: 10, min. poziva: 3, prag gresaka: 50%, cekanje: 10s, poluotvoreno: 3 poziva |
| Retry | `smestajService` | Maks. pokusaji: 3, cekanje: 500ms |
| Time Limiter | `smestajService` | Timeout: 5s |
| Rate Limiter | `createWorkOrder` | 5 zahteva / 60s |
| Rate Limiter | `updateWorkOrderStatus` | 10 zahteva / 60s |
| Rate Limiter | `createMaintenanceTask` | 3 zahteva / 60s |
| Rate Limiter | `createWorker` | 3 zahteva / 60s |

---

## Implementirani endpointi

### WorkOrderController — `/api/workOrder`

| Metoda | Putanja | Opis |
|---|---|---|
| POST | `/api/workOrder/save` | Kreira novi radni nalog — validira postojanje jedinice u Smestaj servisu (rate limiter: 5/60s) |
| GET | `/api/workOrder/all` | Vraca sve radne naloge |
| GET | `/api/workOrder/status/{id}` | Vraca status radnog naloga po ID-ju (`PENDING`, `IN_PROGRESS`, `COMPLETED`) |
| PATCH | `/api/workOrder/updateStatus/{id}?notes={notes}` | Unapredjuje status naloga za jedan korak; pri prelasku u `COMPLETED` kreira log unos (rate limiter: 10/60s) |
| GET | `/api/workOrder/byUnit/{unitId}` | Vraca sve radne naloge za zadatu smestajnu jedinicu |
| GET | `/api/workOrder/statistics/{unitId}` | Vraca statistike odrzavanja za jedinicu (ukupan broj, broj zavrṣenih, prosecno trajanje, datum poslednjeg odrzavanja) |
| GET | `/api/workOrder/inProgress` | Vraca sve radne naloge sa statusom `IN_PROGRESS` |

### WorkerController — `/api/worker`

| Metoda | Putanja | Opis |
|---|---|---|
| POST | `/api/worker/save` | Kreira novog radnika (rate limiter: 3/60s) |
| GET | `/api/worker/workers?speciality={spec}` | Vraca radnike po specijalizaciji; ako `speciality` nije naveden, vraca sve |
| DELETE | `/api/worker/{id}` | Brise radnika po ID-ju |
| GET | `/api/worker/statsBySpeciality` | Vraca statistike radnika grupisane po specijalizaciji |

### MaintenanceTaskController — `/api/maintenanceTask`

| Metoda | Putanja | Opis |
|---|---|---|
| POST | `/api/maintenanceTask/save` | Kreira novi tip zadatka odrzavanja sa procenjenim trajanjem (rate limiter: 3/60s) |
| GET | `/api/maintenanceTask/all` | Vraca sve tipove zadataka odrzavanja |

### MaintenanceLogController — `/api/maintenanceLog`

| Metoda | Putanja | Opis |
|---|---|---|
| GET | `/api/maintenanceLog/avgMaintenanceDuration/{unitId}` | Vraca prosecno trajanje odrzavanja u satima za zadatu smestajnu jedinicu |

---

## Mrezni port

Servis operise na portu **`8082`**.

Konfiguracija se ucitava sa Config Servera pri pokretanju. Relevantna datoteka:
`ConfigService/src/main/resources/config/odrzavanje.properties`

| Resurs | URL |
|---|---|
| Swagger UI | http://localhost:8082/swagger-ui.html |
| H2 konzola | http://localhost:8082/h2-console |
| Health check | http://localhost:8082/actuator/health |
| Circuit breakers | http://localhost:8082/actuator/circuitbreakers |
| Rate limiters | http://localhost:8082/actuator/ratelimiters |

---

## Lokalno pokretanje i testiranje

### Preduslovi

- JDK 17+
- Maven 3.9+
- Pokrenuti **Config Server** (port `8888`) pre pokretanja ovog servisa

### Redosled pokretanja

Config Server mora biti pokrenut prvi jer ostali servisi ucitavaju konfiguraciju sa njega pri startu. Odrzavanje servis poziva Smestaj servis, pa je preporuceni redosled:

```bash
# 1. Pokrenuti Config Server
cd ConfigService
mvn spring-boot:run

# 2. Pokrenuti Smestaj servis (jer Odrzavanje validira jedinice na njemu)
cd smestaj
mvn spring-boot:run

# 3. Pokrenuti Odrzavanje servis
cd odrzavanje
mvn spring-boot:run
```

Alternativno iz IDE (IntelliJ IDEA / Eclipse):
1. Importovati projekat kao Maven projekat
2. Pokrenuti `Main.java` iz `ConfigService` modula
3. Pokrenuti `UnitApplication.java` iz `smestaj` modula
4. Pokrenuti `Main.java` iz `odrzavanje` modula

> Napomena: Odrzavanje servis koristi circuit breaker za pozive ka Smestaj servisu — moze da se pokrene i bez Smestaj servisa, ali ce validacija jedinica vracati `ServiceUnavailableException`.

### Provera uspesnog pokretanja

```bash
curl http://localhost:8082/actuator/health
```

Ocekivani odgovor: `{"status":"UP"}`

### Primeri testiranja endpointa

#### Dobaviti sve radne naloge
```bash
curl -X GET http://localhost:8082/api/workOrder/all
```

#### Kreirati novi radni nalog
```bash
curl -X POST http://localhost:8082/api/workOrder/save \
  -H "Content-Type: application/json" \
  -d '{
    "unitId": 1,
    "scheduledFor": "2026-05-10T10:00:00",
    "workerId": 1,
    "maintenanceTaskId": 1
  }'
```

#### Promeniti status radnog naloga (sa napomenom)
```bash
curl -X PATCH "http://localhost:8082/api/workOrder/updateStatus/1?notes=Popravka%20uspesno%20zavrsena"
```

#### Dobaviti radne naloge za smestajnu jedinicu
```bash
curl -X GET http://localhost:8082/api/workOrder/byUnit/1
```

#### Statistike odrzavanja za jedinicu
```bash
curl -X GET http://localhost:8082/api/workOrder/statistics/1
```

#### Kreirati novog radnika
```bash
curl -X POST http://localhost:8082/api/worker/save \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Nikola",
    "lastName": "Nikolic",
    "speciality": "elektricar"
  }'
```

#### Dobaviti radnike po specijalizaciji
```bash
curl -X GET "http://localhost:8082/api/worker/workers?speciality=elektricar"
```

#### Statistike po specijalizaciji
```bash
curl -X GET http://localhost:8082/api/worker/statsBySpeciality
```

#### Kreirati tip zadatka
```bash
curl -X POST http://localhost:8082/api/maintenanceTask/save \
  -H "Content-Type: application/json" \
  -d '{
    "taskName": "Popravka vodovoda",
    "estimatedDurationHours": 3
  }'
```

#### Prosecno trajanje odrzavanja za jedinicu
```bash
curl -X GET http://localhost:8082/api/maintenanceLog/avgMaintenanceDuration/1
```

### Baza podataka

Baza se automatski inicijalizuje sa test podacima pri svakom pokretanju (`data.sql`):
- 5 radnika (2 elektricara, 3 higjenicara)
- 6 tipova zadataka odrzavanja
- 7 radnih naloga u razlicitim statusima (PENDING, IN_PROGRESS, COMPLETED)
- 3 log unosa za zavrsene radne naloge

Pristup H2 konzoli: http://localhost:8082/h2-console

| Polje | Vrednost |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Korisnicko ime | `odrzavanjeservis` |
| Lozinka | `odrzavanjeservis` |

### Pokretanje testova

```bash
cd odrzavanje
mvn test
```
