CREATE TABLE IF NOT EXISTS products
(
    id                     VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    name                   VARCHAR(128) NOT NULL,
    description            VARCHAR      NOT NULL,
    img_link               VARCHAR      NOT NULL,
    price                  VARCHAR      NOT NULL,

    currency               VARCHAR(5)   NOT NULL,
    available              BOOL         DEFAULT TRUE
);