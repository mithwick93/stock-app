-- Sample stock data which will be loaded at the start of the application.

INSERT INTO STOCKS (name, current_price, created_at, last_update)
VALUES ('AMZN', '97.25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('EBAY', '46.66', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('MSFT', '240.22', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('WMT', '140.54', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('HD', '315.00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);