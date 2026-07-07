CREATE TABLE inventories (
    id            BIGSERIAL PRIMARY KEY,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by    UUID,
    updated_by    UUID,
    version       BIGINT,
    product_id    BIGINT NOT NULL,
    warehouse_id  BIGINT NOT NULL,
    quantity      INT NOT NULL DEFAULT 0,
    minimum_stock INT,
    maximum_stock INT,
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    CONSTRAINT fk_inventory_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (id) ON DELETE CASCADE,
    CONSTRAINT uq_inventory_product_warehouse UNIQUE (product_id, warehouse_id)
);
