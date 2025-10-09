CREATE TABLE IF NOT EXISTS crypto.user (
    pk_user_id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS crypto.transaction (
    pk_transaction_id SERIAL PRIMARY KEY,
    fk_user_id INTEGER NOT NULL,
    token_id varchar(50) NOT NULL,
    amount double NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (fk_user_id) REFERENCES crypto.user (pk_user_id)
);

