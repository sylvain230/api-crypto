CREATE TABLE IF NOT EXISTS crypto.transaction (
    pk_transaction_id SERIAL PRIMARY KEY,
    token_id varchar(50) NOT NULL,
    amount double  NOT NULL,
    dateTime dateTime NOT NULL
);