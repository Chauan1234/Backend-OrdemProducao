CREATE TYPE machine_status AS ENUM (
    'AVAILABLE',
    'MAINTENANCE',
    'INACTIVE'
);

CREATE TYPE production_order_status AS ENUM (
    'PENDING',
    'IN_PROGRESS',
    'COMPLETED',
    'CANCELLED'
);

CREATE TABLE tb_product (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    code SERIAL UNIQUE,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tb_machine (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    code SERIAL UNIQUE,
    status machine_status NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE tb_production_order (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES tb_product (id),
    machine_id UUID NOT NULL REFERENCES tb_machine (id),
    target_quantity INTEGER NOT NULL,
    produced_quantity INTEGER NOT NULL DEFAULT 0,
    status production_order_status NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    deadline DATE
);

CREATE INDEX idx_tb_production_order_product_id
    ON tb_production_order (product_id);
CREATE INDEX idx_tb_production_order_machine_id
    ON tb_production_order (machine_id);
CREATE INDEX idx_tb_production_order_status
    ON tb_production_order (status);

CREATE TABLE tb_production_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    production_order_id UUID NOT NULL REFERENCES tb_production_order (id),
    quantity INTEGER NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE INDEX idx_tb_production_record_order_id
    ON tb_production_record (production_order_id);
