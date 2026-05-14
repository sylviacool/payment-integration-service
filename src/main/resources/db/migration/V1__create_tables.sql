CREATE TABLE payment_method (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    status SMALLINT DEFAULT 1,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_type (
    id INT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    status SMALLINT DEFAULT 1,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE provider (
    id SERIAL PRIMARY KEY,
    provider_name VARCHAR(50) NOT NULL,
    status SMALLINT DEFAULT 1,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transaction_status (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    status SMALLINT DEFAULT 1,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transaction_table (
    id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    payment_method_id INT NOT NULL,
    provider_id INT NOT NULL,
    payment_type_id INT NOT NULL,
    txn_status_id INT NOT NULL,

    amount NUMERIC(19,2) DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,

    merchant_transaction_reference VARCHAR(50) NOT NULL,
    txn_reference VARCHAR(50) NOT NULL UNIQUE,
    provider_reference VARCHAR(100),

    error_code VARCHAR(500),
    error_message VARCHAR(1000),

    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    retry_count INT DEFAULT 0,

    CONSTRAINT fk_payment_method
        FOREIGN KEY(payment_method_id)
        REFERENCES payment_method(id),

    CONSTRAINT fk_provider
        FOREIGN KEY(provider_id)
        REFERENCES provider(id),

    CONSTRAINT fk_payment_type
        FOREIGN KEY(payment_type_id)
        REFERENCES payment_type(id),

    CONSTRAINT fk_transaction_status
        FOREIGN KEY(txn_status_id)
        REFERENCES transaction_status(id)
);

CREATE TABLE transaction_log (
    id SERIAL PRIMARY KEY,

    transaction_id INT NOT NULL,

    txn_from_status VARCHAR(50) DEFAULT '-1',
    txn_to_status VARCHAR(50) DEFAULT '-1',

    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction
        FOREIGN KEY(transaction_id)
        REFERENCES transaction_table(id)
);