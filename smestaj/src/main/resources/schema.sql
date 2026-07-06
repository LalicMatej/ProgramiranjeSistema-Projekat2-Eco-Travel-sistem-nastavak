-- ============================================================
--  SMESTAJ MIKROSERVIS  –  Sema baze podataka
--  Baza: H2 in-memory (jdbc:h2:mem:smestajservis)
--  Tabele se automatski kreiraju od strane Hibernate (ddl-auto=create-drop).
--  Ovaj fajl sluzi iskljucivo kao dokumentacija seme.
-- ============================================================


-- ------------------------------------------------------------
-- Tabela: address
-- Opis: Adresa smestajne jedinice (grad, postanski broj, ulica).
--       Svaka smestajna jedinica ima tacno jednu adresu (@OneToOne).
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS address (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    city           VARCHAR(255),
    zip_code       VARCHAR(255),
    street_address VARCHAR(255)
);


-- ------------------------------------------------------------
-- Tabela: unit
-- Opis: Smestajna jedinica (koliba, apartman, vila).
--       Sadrzi naziv, tip, osnovnu cenu po nocenju i vezu ka adresi.
--       Tipovi: KOLIBA | APARTMAN | VILA
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS unit (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(255),
    unit_type             VARCHAR(50),
    base_price_per_night  DOUBLE,
    address_id            BIGINT,

    CONSTRAINT fk_unit_address FOREIGN KEY (address_id) REFERENCES address(id)
);


-- ------------------------------------------------------------
-- Tabela: facility
-- Opis: Sadrzaj / usluga smestajne jedinice (npr. Bazen, WiFi, Sauna).
--       extra_cost je dodatni trosak po nocenju koji se dodaje na osnovnu cenu.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS facility (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255),
    extra_cost DOUBLE
);


-- ------------------------------------------------------------
-- Tabela: unit_facilities  (join tabela za @ManyToMany: Unit <-> Facility)
-- Opis: Veza izmedju smestajnih jedinica i sadrzaja koje nude.
--       Jedna jedinica moze imati vise sadrzaja i jedan sadrzaj
--       moze biti dostupan u vise jedinica.
-- Napomena: Hibernate generise kolonu "units_id" (sa s) za stranu
--           vlasnika veze jer je polje na Facility strani nazvano "units".
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS unit_facilities (
    units_id     BIGINT NOT NULL,
    facilities_id BIGINT NOT NULL,

    PRIMARY KEY (units_id, facilities_id),
    CONSTRAINT fk_uf_unit     FOREIGN KEY (units_id)      REFERENCES unit(id),
    CONSTRAINT fk_uf_facility FOREIGN KEY (facilities_id) REFERENCES facility(id)
);


-- ------------------------------------------------------------
-- Tabela: price_tier
-- Opis: Sezonski cenovni nivo za smestajnu jedinicu.
--       multiplier se mnozi sa osnovnom cenom + troškovima sadrzaja
--       za sve nocevi u datom periodu.
--       Validacija: datumi ne smeju da se preklapaju za istu jedinicu,
--       a trajanje ne sme biti duze od 365 dana.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS price_tier (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255),
    multiplier  DOUBLE,
    start_date  DATE,
    end_date    DATE,
    unit_id     BIGINT,

    CONSTRAINT fk_price_tier_unit FOREIGN KEY (unit_id) REFERENCES unit(id)
);


-- ------------------------------------------------------------
-- Tabela: availability_calendar
-- Opis: Kalendar rezervacija i blokiranih termina za smestajne jedinice.
--       reason opisuje razlog blokiranja (npr. "Rezervisano", "Odrzavanje").
--       Validacija: novi termin ne sme da se preklapa sa postojecim
--       za istu smestajnu jedinicu.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS availability_calendar (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE,
    end_date   DATE,
    reason     VARCHAR(255),
    unit_id    BIGINT,

    CONSTRAINT fk_availability_unit FOREIGN KEY (unit_id) REFERENCES unit(id)
);
