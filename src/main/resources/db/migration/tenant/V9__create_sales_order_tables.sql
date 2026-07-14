CREATE TABLE sales_order (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(255) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    shipping_address TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_sales_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_sales_order_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id)
);

CREATE INDEX idx_sales_order_customer ON sales_order(customer_id);
CREATE INDEX idx_sales_order_warehouse ON sales_order(warehouse_id);
CREATE INDEX idx_sales_order_status ON sales_order(status);

CREATE TABLE sales_order_item (
    id BIGSERIAL PRIMARY KEY,
    sales_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_so_item_order FOREIGN KEY (sales_order_id) REFERENCES sales_order(id),
    CONSTRAINT fk_so_item_product FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE INDEX idx_so_item_order ON sales_order_item(sales_order_id);
CREATE INDEX idx_so_item_product ON sales_order_item(product_id);
