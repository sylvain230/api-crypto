CREATE TABLE IF NOT EXISTS crypto.app_user (
    pk_user_id SERIAL PRIMARY KEY,
    password_hash varchar(255) NOT NULL,
    username varchar(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS crypto.token_metadata (
    token_id VARCHAR(50) PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS crypto.transaction (
    pk_transaction_id SERIAL PRIMARY KEY,
    fk_user_id INTEGER NOT NULL,
    token_id varchar(50) NOT NULL,
    amount NUMERIC(20, 10) NOT NULL,
    price NUMERIC(20, 10) NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (fk_user_id) REFERENCES crypto.user (pk_user_id)
    CONSTRAINT fk_token_id FOREIGN KEY (token_id) REFERENCES token_metadata (token_id)
);

CREATE TABLE IF NOT EXISTS crypto.historical_data(
	pk_historical_data_di SERIAL PRIMARY KEY,
	token_id varchar(50) not null,
	historical JSONB,
	last_updated TIMESTAMP NOT NULL,
	CONSTRAINT fk_token_id_hist FOREIGN KEY (token_id) REFERENCES crypto.token_metadata (token_id)
);

CREATE UNIQUE INDEX idx_token_id ON crypto.historical_data (token_id);

insert into token_metadata values ('the-graph', 'GRT', 'The Graph');
insert into token_metadata values ('bitcoin', 'BTC', 'Bitcoin');
insert into token_metadata values ('ethereum', 'ETH', 'Ethereum');

