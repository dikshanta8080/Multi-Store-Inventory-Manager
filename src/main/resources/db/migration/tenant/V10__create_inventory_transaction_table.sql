CREATE TABLE inventory_transaction (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    quantity_change INT NOT NULL,
    reference_number VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_inv_trans_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_inv_trans_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id)
);

CREATE INDEX idx_inv_trans_product ON inventory_transaction(product_id);
CREATE INDEX idx_inv_trans_warehouse ON inventory_transaction(warehouse_id);
CREATE INDEX idx_inv_trans_type ON inventory_transaction(transaction_type);
