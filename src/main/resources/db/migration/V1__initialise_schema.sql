CREATE TABLE stocks
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)             NOT NULL,
    current_price DECIMAL(19, 4)           NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update   TIMESTAMP WITH TIME ZONE NOT NULL
);