CREATE TABLE provider (

    id BIGSERIAL PRIMARY KEY,

    provider_name VARCHAR(50) NOT NULL UNIQUE

);



CREATE TABLE transaction_status (

    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL UNIQUE

);



CREATE TABLE transaction_table (

    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    amount NUMERIC(19,2) NOT NULL,

    currency VARCHAR(3) NOT NULL,



    txn_reference VARCHAR(100) NOT NULL UNIQUE,

    paypal_order_id VARCHAR(100),



    provider_id BIGINT NOT NULL,

    txn_status_id BIGINT NOT NULL,



    error_code VARCHAR(100),

    error_message VARCHAR(500),



    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,



    CONSTRAINT fk_provider
        FOREIGN KEY (provider_id)
        REFERENCES provider(id),



    CONSTRAINT fk_transaction_status
        FOREIGN KEY (txn_status_id)
        REFERENCES transaction_status(id)

);