merge into guests (id, first_name, last_name, email)
key (id)
values (1, 'Ana', 'Markovic', 'ana.markovic@raf.rs');

merge into cancellation_policies (id, policy_name, refund_percentage)
key (id)
values (1, 'Flexible', 100.00);

merge into bookings (id, unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
key (id)
values (1, 101, 1, 1, date '2026-05-10', date '2026-05-15', 475.00, 'PENDING');

merge into add_on_items (id, booking_id, service_name, price)
key (id)
values (1, 1, 'Breakfast', 25.00);

merge into booking_status_logs (id, booking_id, old_status, new_status, changed_at, note)
key (id)
values (1, 1, null, 'PENDING', CURRENT_TIMESTAMP, 'Initial booking state');

alter table guests alter column id restart with 100;
alter table cancellation_policies alter column id restart with 100;
alter table bookings alter column id restart with 100;
alter table add_on_items alter column id restart with 100;
alter table booking_status_logs alter column id restart with 100;
