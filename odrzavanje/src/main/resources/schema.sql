-- ============================================================
--  ODRZAVANJE MIKROSERVIS  –  Sema baze podataka
--  Baza: H2 in-memory (jdbc:h2:mem:testdb)
--  Tabele se automatski kreiraju od strane Hibernate (ddl-auto=create-drop).
--  Ovaj fajl sluzi iskljucivo kao dokumentacija seme.
-- ============================================================


-- ------------------------------------------------------------
-- Tabela: worker
-- Opis: Radnik koji izvrsava odrzavanje smestajnih jedinica.
--       speciality opisuje oblast (npr. "elektricar", "higijenicar").
--       Sistem proverava dostupnost radnika pri zakazivanju
--       (vremenski bafer od 1h izmedju naloga).
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS worker (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    speciality  VARCHAR(255)
);


-- ------------------------------------------------------------
-- Tabela: maintenance_task
-- Opis: Katalog tipova zadataka odrzavanja sa procenjenim trajanjem.
--       estimated_duration_hours sluzi za pracenje i statistike.
--       Primeri: "Popravka klime" (2h), "Generalno čišćenje" (3h).
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS maintenance_task (
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_name                VARCHAR(255),
    estimated_duration_hours INTEGER
);


-- ------------------------------------------------------------
-- Tabela: work_order
-- Opis: Radni nalog za odrzavanje smestajne jedinice.
--       unit_id je referenca ka smestaj-servisu (bez FK ogranicenja
--       jer su servisi odvojeni — validacija se vrsi putem Feign poziva).
--       status (INTEGER, ORDINAL enum):
--         0 = PENDING      – nalog kreiran, ceka na izvrsenje
--         1 = IN_PROGRESS  – odrzavanje u toku
--         2 = COMPLETED    – odrzavanje zavrseno (kreira se MaintenanceLog)
--       Prelaz stanja: PENDING -> IN_PROGRESS -> COMPLETED
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS work_order (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_id             BIGINT,
    status              INTEGER,
    scheduled_for       TIMESTAMP,
    worker_id           BIGINT,
    maintenance_task_id BIGINT,

    CONSTRAINT fk_wo_worker           FOREIGN KEY (worker_id)           REFERENCES worker(id),
    CONSTRAINT fk_wo_maintenance_task FOREIGN KEY (maintenance_task_id) REFERENCES maintenance_task(id)
);


-- ------------------------------------------------------------
-- Tabela: maintenance_log
-- Opis: Dnevnik zavrsenih odrzavanja. Unos se automatski kreira
--       kada radni nalog predje u status COMPLETED (Observer obrazac).
--       unit_id je referenca ka smestaj-servisu (bez FK ogranicenja).
--       completed_at biljezi tacno vreme zavrsetka odrzavanja.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS maintenance_log (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_id          BIGINT,
    task_description VARCHAR(255),
    worker_name      VARCHAR(255),
    completed_at     TIMESTAMP,
    notes            VARCHAR(1000),
    work_order_id    BIGINT,

    CONSTRAINT fk_ml_work_order FOREIGN KEY (work_order_id) REFERENCES work_order(id)
);
