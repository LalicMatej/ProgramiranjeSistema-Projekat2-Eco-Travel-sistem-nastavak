-- Workers
INSERT IGNORE INTO worker (id, first_name, last_name, speciality) VALUES
    (1, 'Marko',  'Marković',  'elektricar'),
    (2, 'Jovan',  'Jovanović', 'elektricar'),
    (3, 'Ana',    'Anić',      'higijenicar'),
    (4, 'Petar',  'Petrović',  'higijenicar'),
    (5, 'Milica', 'Milić',     'higijenicar');

-- Maintenance Tasks
INSERT IGNORE INTO maintenance_task (id, task_name, estimated_duration_hours) VALUES
    (1, 'Popravka klime',        2),
    (2, 'Generalno čišćenje',    3),
    (3, 'Popravka grejanja',     1),
    (4, 'Dubinsko čišćenje',     4),
    (5, 'Popravka elektrike',    2),
    (6, 'Čišćenje ventilacije',  2);

-- Work Orders (status: 0=PENDING, 1=IN_PROGRESS, 2=COMPLETED)
INSERT IGNORE INTO work_order (id, unit_id, status, scheduled_for, worker_id, maintenance_task_id) VALUES
    (1, 1, 2, '2026-03-20T10:00:00', 1, 1),
    (2, 2, 1, '2026-03-24T09:00:00', 2, 2),
    (3, 1, 0, '2026-03-25T13:00:00', 3, 4),
    (4, 3, 2, '2026-03-22T14:00:00', 1, 3),
    (5, 4, 2, '2026-03-23T11:00:00', 4, 2),
    (6, 1, 0, '2026-03-26T15:00:00', 5, 6),
    (7, 5, 1, '2026-03-24T13:00:00', 2, 5);

-- Maintenance Logs
INSERT IGNORE INTO maintenance_log (id, unit_id, task_description, worker_name, completed_at, notes, work_order_id) VALUES
    (1, 1, 'Popravka klime',    'Marko Marković', '2026-03-20T12:30:00', 'Klima uspešno popravljena, radi normalno', 1),
    (2, 2, 'Popravka grejanja', 'Marko Marković', '2026-03-22T15:20:00', 'Grejanje zamenjen termostat',             4),
    (3, 3, 'Generalno čišćenje','Petar Petrović', '2026-03-23T15:30:00', 'Sve očišćeno, zadovoljan stanar',         5);

-- Postavi auto-increment sekvence iznad seed podataka
ALTER TABLE worker           AUTO_INCREMENT = 6;
ALTER TABLE maintenance_task AUTO_INCREMENT = 7;
ALTER TABLE work_order       AUTO_INCREMENT = 8;
ALTER TABLE maintenance_log  AUTO_INCREMENT = 4;
