-- Sample stock data which will be loaded at the start of the application.

INSERT INTO stocks (name, current_price, created_at, last_update)
VALUES ('XOM', '12.34', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('GE', '9.99', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('MSFT', '70.45', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('CSCO', '110.45', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('WMT', '40', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);