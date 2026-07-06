-- 1. Ubaci adrese
INSERT IGNORE INTO address (id, city, zip_code, street_address) VALUES
    (1, 'Novi Sad', '21000', 'Bulevar Oslobođenja 1'),
    (2, 'Beograd', '11000', 'Knez Mihailova 5'),
    (3, 'Zlatibor', '31310', 'Naselje Čigota bb'),
    (4, 'Kopaonik', '36354', 'Ski centar bb');

-- 2. Ubaci unit-e
INSERT IGNORE INTO unit (id, name, unit_type, base_price_per_night, address_id) VALUES
    (1, 'Brvnara Bor', 'KOLIBA', 100.0, 1),
    (2, 'Apartman Sunny', 'APARTMAN', 150.0, 2),
    (3, 'Vila Zlatibor', 'VILA', 200.0, 3),
    (4, 'Planinska kuća', 'KOLIBA', 120.0, 4);

-- 3. Ubaci facility-je
INSERT IGNORE INTO facility (id, name, extra_cost) VALUES
    (1, 'Bazen', 50.0),
    (2, 'WiFi', 0.0),
    (3, 'Sauna', 30.0),
    (4, 'Džakuzi', 70.0),
    (5, 'Klima', 10.0),
    (6, 'Parking', 5.0),
    (7, 'Spa centar', 100.0),
    (8, 'Teretana', 20.0);

-- 4. Poveži unit-e sa facility-jima
-- Brvnara Bor (id=1): WiFi, Klima, Parking
INSERT IGNORE INTO unit_facilities (units_id, facilities_id) VALUES
    (1, 2), (1, 5), (1, 6);

-- Apartman Sunny (id=2): WiFi, Klima, Parking, Teretana
INSERT IGNORE INTO unit_facilities (units_id, facilities_id) VALUES
    (2, 2), (2, 5), (2, 6), (2, 8);

-- Vila Zlatibor (id=3): Bazen, WiFi, Sauna, Džakuzi, Klima, Parking, Spa
INSERT IGNORE INTO unit_facilities (units_id, facilities_id) VALUES
    (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7);

-- Planinska kuća (id=4): WiFi, Klima, Parking
INSERT IGNORE INTO unit_facilities (units_id, facilities_id) VALUES
    (4, 2), (4, 5), (4, 6);

-- 5. Ubaci price tier-ove (sezonske cene)
-- Brvnara Bor (unit_id=1)
INSERT IGNORE INTO price_tier (id, name, multiplier, start_date, end_date, unit_id) VALUES
    (1,  'Letnja sezona', 1.5, '2026-06-01', '2026-08-31', 1),
    (2,  'Zimska sezona', 1.3, '2026-12-01', '2027-02-28', 1),
    (3,  'Van sezone',    1.0, '2026-03-01', '2026-05-31', 1),
    (4,  'Van sezone',    1.0, '2026-09-01', '2026-11-30', 1);

-- Apartman Sunny (unit_id=2)
INSERT IGNORE INTO price_tier (id, name, multiplier, start_date, end_date, unit_id) VALUES
    (5,  'Letnja sezona', 1.4, '2026-06-01', '2026-08-31', 2),
    (6,  'Zimska sezona', 1.2, '2026-12-01', '2027-02-28', 2),
    (7,  'Van sezone',    1.0, '2026-03-01', '2026-05-31', 2),
    (8,  'Van sezone',    1.0, '2026-09-01', '2026-11-30', 2);

-- Vila Zlatibor (unit_id=3)
INSERT IGNORE INTO price_tier (id, name, multiplier, start_date, end_date, unit_id) VALUES
    (9,  'Letnja sezona', 1.6, '2026-06-01', '2026-08-31', 3),
    (10, 'Zimska sezona', 1.8, '2026-12-01', '2027-03-15', 3),
    (11, 'Van sezone',    1.2, '2026-04-01', '2026-05-31', 3),
    (12, 'Van sezone',    1.2, '2026-09-01', '2026-11-30', 3);

-- Planinska kuća (unit_id=4)
INSERT IGNORE INTO price_tier (id, name, multiplier, start_date, end_date, unit_id) VALUES
    (13, 'Letnja sezona', 1.3, '2026-06-01', '2026-08-31', 4),
    (14, 'Zimska sezona', 1.7, '2026-12-01', '2027-03-01', 4),
    (15, 'Van sezone',    1.0, '2026-04-01', '2026-05-31', 4),
    (16, 'Van sezone',    1.0, '2026-09-01', '2026-11-30', 4);

-- 6. Ubaci availability calendar
-- Svaki unit ima dva unosa: jedan sa time_slot_id=1, drugi sa time_slot_id=2
-- Ostali unosi (regularne rezervacije) imaju time_slot_id=NULL

-- Brvnara Bor (unit_id=1)
INSERT IGNORE INTO availability_calendar (id, start_date, end_date, reason, unit_id, work_order_id, time_slot_id) VALUES
    (1, '2026-07-10', '2026-07-20', 'Rezervisano', 1, NULL, NULL),
    (2, '2026-08-01', '2026-08-10', 'Blokada - avantura termin', 1, NULL, 1),
    (3, '2026-12-25', '2026-12-30', 'Blokada - avantura termin', 1, NULL, 2);

-- Apartman Sunny (unit_id=2)
INSERT IGNORE INTO availability_calendar (id, start_date, end_date, reason, unit_id, work_order_id, time_slot_id) VALUES
    (4, '2026-07-05', '2026-07-15', 'Rezervisano', 2, NULL, NULL),
    (5, '2026-08-15', '2026-08-25', 'Blokada - avantura termin', 2, NULL, NULL),
    (6, '2026-09-01', '2026-09-05', 'Blokada - avantura termin', 2, NULL, NULL);

-- Vila Zlatibor (unit_id=3)
INSERT IGNORE INTO availability_calendar (id, start_date, end_date, reason, unit_id, work_order_id, time_slot_id) VALUES
    (7, '2026-07-01', '2026-07-31', 'Rezervisano - ceo jul', 3, NULL, NULL),
    (8, '2026-12-20', '2027-01-10', 'Blokada - avantura termin', 3, NULL, NULL),
    (9, '2026-11-01', '2026-11-05', 'Blokada - avantura termin', 3, NULL, NULL);

-- Planinska kuća (unit_id=4)
INSERT IGNORE INTO availability_calendar (id, start_date, end_date, reason, unit_id, work_order_id, time_slot_id) VALUES
    (10, '2026-07-18', '2026-07-25', 'Rezervisano', 4, NULL, NULL),
    (11, '2026-08-05', '2026-08-12', 'Blokada - avantura termin', 4, NULL, NULL),
    (12, '2026-09-10', '2026-09-15', 'Blokada - avantura termin', 4, NULL, NULL);

-- Postavi auto-increment sekvence iznad seed podataka
ALTER TABLE address AUTO_INCREMENT = 5;
ALTER TABLE unit AUTO_INCREMENT = 5;
ALTER TABLE facility AUTO_INCREMENT = 9;
ALTER TABLE price_tier AUTO_INCREMENT = 17;
ALTER TABLE availability_calendar AUTO_INCREMENT = 13;