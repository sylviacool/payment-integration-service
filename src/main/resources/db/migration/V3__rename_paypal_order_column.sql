ALTER TABLE transaction_table
RENAME COLUMN paypal_order_id TO provider_reference;

ALTER TABLE transaction_table
ADD CONSTRAINT uk_provider_reference UNIQUE(provider_reference);