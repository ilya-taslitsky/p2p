CREATE TABLE clients (
                        id VARCHAR(255) PRIMARY KEY,
                        exchange VARCHAR(255),
                        time_to_delete TIMESTAMP,
                        delete_description VARCHAR(255),
                        creation_time TIMESTAMP
);
CREATE TABLE exchanges (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255)
);

CREATE TABLE currencies (
                            id SERIAL PRIMARY KEY,
                            basic_name VARCHAR(255),
                            exchange_name VARCHAR(255)
);
CREATE TABLE payment_methods (
                                 id SERIAL PRIMARY KEY,
                                 basic_name VARCHAR(255),
                                 exchange_name VARCHAR(255),
                                 exchange_id BIGINT,
                                 FOREIGN KEY (exchange_id) REFERENCES exchanges(id) ON DELETE SET NULL
);
CREATE TABLE payment_methods_currencies (
                                            payment_method_id SERIAL,
                                            currency_id SERIAL,
                                            PRIMARY KEY (payment_method_id, currency_id),
                                            FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE CASCADE,
                                            FOREIGN KEY (currency_id) REFERENCES currencies(id) ON DELETE CASCADE
);

INSERT INTO exchanges (name) VALUES ('BINANCE');
INSERT INTO exchanges (name) VALUES ('BYBIT');
INSERT INTO exchanges (name) VALUES ('HUOBI');
INSERT INTO exchanges (name) VALUES ('OKX');

ALTER TABLE clients
    ADD COLUMN exchange_id BIGINT;

ALTER TABLE clients
    ADD CONSTRAINT fk_exchange
        FOREIGN KEY (exchange_id) REFERENCES exchanges(id) ON DELETE SET NULL;


UPDATE clients
SET exchange_id = exchanges.id
    FROM exchanges
WHERE clients.exchange = exchanges.name;


ALTER TABLE clients
DROP COLUMN exchange;







