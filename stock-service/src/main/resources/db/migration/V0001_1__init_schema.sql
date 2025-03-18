CREATE SCHEMA IF NOT EXISTS stock;

CREATE SEQUENCE stock.sq_order START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE stock.sq_order_item START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE stock.sq_product START WITH 1 INCREMENT BY 1;

CREATE TABLE stock.product
(
    id                BIGINT         NOT NULL DEFAULT NEXTVAL('stock.sq_product') PRIMARY KEY,
    version           INTEGER        NOT NULL,
    name              VARCHAR(255)   NOT NULL,
    quantity_in_stock INTEGER        NOT NULL,
    price             NUMERIC(19, 2) NOT NULL
);

CREATE TYPE stock.order_status AS ENUM ('PENDING', 'CANCELED', 'EXPIRED', 'PAID');

CREATE TABLE stock."order"
(
    id             BIGINT             NOT NULL DEFAULT NEXTVAL('stock.sq_order') PRIMARY KEY,
    status         stock.order_status NOT NULL,
    reserved_until TIMESTAMP,
    paid_at        TIMESTAMP
);

CREATE TABLE stock.order_item
(
    id         BIGINT  NOT NULL DEFAULT NEXTVAL('stock.sq_order_item') PRIMARY KEY,
    order_id   BIGINT  NOT NULL,
    product_id BIGINT  NOT NULL,
    quantity   INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES stock."order" (id),
    FOREIGN KEY (product_id) REFERENCES stock.product (id)
);

