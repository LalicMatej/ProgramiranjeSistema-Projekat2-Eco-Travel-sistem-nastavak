merge into tax_rates (id, tax_name, percentage)
key (id)
values (1, 'VAT', 20.00);

merge into tax_rates (id, tax_name, percentage)
key (id)
values (2, 'CITY_TAX', 5.00);

merge into payment_methods (id, method_name, is_active)
key (id)
values (1, 'Credit Card', true);

merge into invoices (id, booking_id, tax_rate_id, subtotal, total_amount, status)
key (id)
values (1, 1, 1, 475.00, 570.00, 'ISSUED');

merge into transactions (id, invoice_id, payment_method_id, amount, transaction_date)
key (id)
values (1, 1, 1, 570.00, CURRENT_TIMESTAMP);

alter table tax_rates alter column id restart with 100;
alter table payment_methods alter column id restart with 100;
alter table invoices alter column id restart with 100;
alter table transactions alter column id restart with 100;
