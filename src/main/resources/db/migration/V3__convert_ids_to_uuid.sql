-- Keep the original integer IDs until all dependent UUIDs have been populated.
ALTER TABLE tb_product ADD COLUMN uuid_id UUID DEFAULT gen_random_uuid();
ALTER TABLE tb_machine ADD COLUMN uuid_id UUID DEFAULT gen_random_uuid();
ALTER TABLE tb_production_order ADD COLUMN uuid_id UUID DEFAULT gen_random_uuid();
ALTER TABLE tb_production_record ADD COLUMN uuid_id UUID DEFAULT gen_random_uuid();

ALTER TABLE tb_production_order ADD COLUMN uuid_product_id UUID;
ALTER TABLE tb_production_order ADD COLUMN uuid_machine_id UUID;
ALTER TABLE tb_production_record ADD COLUMN uuid_production_order_id UUID;

UPDATE tb_production_order o
SET uuid_product_id = p.uuid_id,
    uuid_machine_id = m.uuid_id
FROM tb_product p, tb_machine m
WHERE p.id = o.product_id
  AND m.id = o.machine_id;

UPDATE tb_production_record r
SET uuid_production_order_id = o.uuid_id
FROM tb_production_order o
WHERE o.id = r.production_order_id;

ALTER TABLE tb_production_order
    DROP CONSTRAINT tb_production_order_product_id_fkey,
    DROP CONSTRAINT tb_production_order_machine_id_fkey;
ALTER TABLE tb_production_record
    DROP CONSTRAINT tb_production_record_production_order_id_fkey;

DROP INDEX idx_tb_production_order_product_id;
DROP INDEX idx_tb_production_order_machine_id;
DROP INDEX idx_tb_production_record_order_id;

ALTER TABLE tb_product DROP CONSTRAINT tb_product_pkey;
ALTER TABLE tb_machine DROP CONSTRAINT tb_machine_pkey;
ALTER TABLE tb_production_order DROP CONSTRAINT tb_production_order_pkey;
ALTER TABLE tb_production_record DROP CONSTRAINT tb_production_record_pkey;

ALTER TABLE tb_product RENAME COLUMN id TO legacy_id;
ALTER TABLE tb_product RENAME COLUMN uuid_id TO id;
ALTER TABLE tb_machine RENAME COLUMN id TO legacy_id;
ALTER TABLE tb_machine RENAME COLUMN uuid_id TO id;
ALTER TABLE tb_production_order RENAME COLUMN id TO legacy_id;
ALTER TABLE tb_production_order RENAME COLUMN uuid_id TO id;
ALTER TABLE tb_production_record RENAME COLUMN id TO legacy_id;
ALTER TABLE tb_production_record RENAME COLUMN uuid_id TO id;

ALTER TABLE tb_production_order RENAME COLUMN product_id TO legacy_product_id;
ALTER TABLE tb_production_order RENAME COLUMN machine_id TO legacy_machine_id;
ALTER TABLE tb_production_order RENAME COLUMN uuid_product_id TO product_id;
ALTER TABLE tb_production_order RENAME COLUMN uuid_machine_id TO machine_id;
ALTER TABLE tb_production_record RENAME COLUMN production_order_id TO legacy_production_order_id;
ALTER TABLE tb_production_record RENAME COLUMN uuid_production_order_id TO production_order_id;

ALTER TABLE tb_production_order
    ALTER COLUMN product_id SET NOT NULL,
    ALTER COLUMN machine_id SET NOT NULL;
ALTER TABLE tb_production_record
    ALTER COLUMN production_order_id SET NOT NULL;

ALTER TABLE tb_product DROP COLUMN legacy_id;
ALTER TABLE tb_machine DROP COLUMN legacy_id;
ALTER TABLE tb_production_order DROP COLUMN legacy_id;
ALTER TABLE tb_production_record DROP COLUMN legacy_id;
ALTER TABLE tb_production_order DROP COLUMN legacy_product_id;
ALTER TABLE tb_production_order DROP COLUMN legacy_machine_id;
ALTER TABLE tb_production_record DROP COLUMN legacy_production_order_id;

ALTER TABLE tb_product
    ADD CONSTRAINT tb_product_pkey PRIMARY KEY (id);
ALTER TABLE tb_machine
    ADD CONSTRAINT tb_machine_pkey PRIMARY KEY (id);
ALTER TABLE tb_production_order
    ADD CONSTRAINT tb_production_order_pkey PRIMARY KEY (id),
    ADD CONSTRAINT tb_production_order_product_id_fkey
        FOREIGN KEY (product_id) REFERENCES tb_product (id),
    ADD CONSTRAINT tb_production_order_machine_id_fkey
        FOREIGN KEY (machine_id) REFERENCES tb_machine (id);
ALTER TABLE tb_production_record
    ADD CONSTRAINT tb_production_record_pkey PRIMARY KEY (id),
    ADD CONSTRAINT tb_production_record_production_order_id_fkey
        FOREIGN KEY (production_order_id) REFERENCES tb_production_order (id);

CREATE INDEX idx_tb_production_order_product_id
    ON tb_production_order (product_id);
CREATE INDEX idx_tb_production_order_machine_id
    ON tb_production_order (machine_id);
CREATE INDEX idx_tb_production_record_order_id
    ON tb_production_record (production_order_id);
