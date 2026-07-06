INSERT IGNORE INTO guests (id, first_name, last_name, email)
VALUES
  (1, 'Ana', 'Markovic', 'ana.markovic@raf.rs'),
  (2, 'Petar', 'Petrovic', 'petar.petrovic@raf.rs'),
  (3, 'Milica', 'Nikolic', 'milica.nikolic@raf.rs');

INSERT IGNORE INTO cancellation_policies (id, policy_name, refund_percentage)
VALUES
  (1, 'Flexible', 100.00),
  (2, 'Moderate', 50.00),
  (3, 'Strict', 0.00);

INSERT IGNORE INTO bookings (id, unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
VALUES
  (1, 101, 1, 1, '2026-06-10', '2026-06-15', 475.00, 'PENDING'),
  (2, 102, 2, 2, '2026-07-01', '2026-07-06', 650.00, 'CONFIRMED'),
  (3, 103, 3, 3, '2026-08-12', '2026-08-18', 840.00, 'COMPLETED');

INSERT IGNORE INTO add_on_items (id, booking_id, service_name, price)
VALUES
  (1, 1, 'Breakfast', 25.00),
  (2, 2, 'Airport transfer', 40.00),
  (3, 3, 'Guided local tour', 75.00);

INSERT IGNORE INTO booking_status_logs (id, booking_id, old_status, new_status, changed_at, note)
VALUES
  (1, 1, NULL, 'PENDING', CURRENT_TIMESTAMP, 'Initial booking state'),
  (2, 2, 'PENDING', 'CONFIRMED', CURRENT_TIMESTAMP, 'Booking confirmed by host'),
  (3, 3, 'CONFIRMED', 'COMPLETED', CURRENT_TIMESTAMP, 'Guest checked out');

ALTER TABLE guests AUTO_INCREMENT = 100;
ALTER TABLE cancellation_policies AUTO_INCREMENT = 100;
ALTER TABLE bookings AUTO_INCREMENT = 100;
ALTER TABLE add_on_items AUTO_INCREMENT = 100;
ALTER TABLE booking_status_logs AUTO_INCREMENT = 100;
