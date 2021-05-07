CREATE TABLE IF NOT EXISTS users
(
    id                     VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    name                   VARCHAR(128) NOT NULL UNIQUE,
    email                  VARCHAR      NOT NULL UNIQUE,
    encoded_password       VARCHAR(128) NOT NULL,
    authorities            VARCHAR      NOT NULL,

    phone                  VARCHAR(128) NOT NULL,
    address                VARCHAR(512) NOT NULL,

    created_at             TIMESTAMP    NOT NULL
);