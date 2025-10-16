CREATE TABLE IF NOT EXISTS crypto.app_user (
    pk_user_id SERIAL PRIMARY KEY,
    password_hash varchar(255) NOT NULL,
    username varchar(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS crypto.transaction (
    pk_transaction_id SERIAL PRIMARY KEY,
    fk_user_id INTEGER NOT NULL,
    token_id varchar(50) NOT NULL,
    amount double NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (fk_user_id) REFERENCES crypto.user (pk_user_id)
);

CREATE TABLE IF NOT EXISTS crypto.historical_data(
	pk_historical_data_di SERIAL PRIMARY KEY,
	token_id varchar(50) not null,
	historical JSONB,
	last_updated TIMESTAMP NOT NULL
)

CREATE UNIQUE INDEX idx_token_id ON crypto.historical_data (token_id)

