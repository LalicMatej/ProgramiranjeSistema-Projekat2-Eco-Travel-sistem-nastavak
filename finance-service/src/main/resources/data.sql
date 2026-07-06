INSERT IGNORE INTO tax_rates (id, tax_name, percentage)
VALUES
  (1, 'VAT', 20.00),
  (2, 'CITY_TAX', 5.00),
  (3, 'SERVICE_TAX', 10.00);

INSERT IGNORE INTO payment_methods (id, method_name, is_active)
VALUES
  (1, 'Credit Card', true),
  (2, 'Bank Transfer', true),
  (3, 'Cash', false);

INSERT IGNORE INTO invoices (id, booking_id, tax_rate_id, subtotal, total_amount, status)
VALUES
  (1, 1, 1, 475.00, 570.00, 'ISSUED'),
  (2, 2, 2, 650.00, 682.50, 'PAID'),
  (3, 3, 1, 840.00, 1008.00, 'DRAFT');

INSERT IGNORE INTO transactions (id, invoice_id, payment_method_id, amount, transaction_date)
VALUES
  (1, 1, 1, 200.00, CURRENT_TIMESTAMP),
  (2, 2, 2, 682.50, CURRENT_TIMESTAMP);

ALTER TABLE tax_rates AUTO_INCREMENT = 100;
ALTER TABLE payment_methods AUTO_INCREMENT = 100;
ALTER TABLE invoices AUTO_INCREMENT = 100;
ALTER TABLE transactions AUTO_INCREMENT = 100;
