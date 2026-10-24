INSERT INTO tb_product (name, description, active) VALUES
    ('Conjunto de válvula industrial', 'Válvula de controle para linhas industriais.', TRUE),
    ('Eixo de transmissão', 'Eixo usinado para equipamentos de manufatura.', TRUE),
    ('Carcaça de bomba', 'Componente fundido para conjunto de bombeamento.', TRUE);

INSERT INTO tb_machine (name, status) VALUES
    ('Torno CNC 01', 'AVAILABLE'),
    ('Centro de usinagem 02', 'MAINTENANCE'),
    ('Prensa hidráulica 03', 'INACTIVE');

INSERT INTO tb_production_order (
    product_id, machine_id, target_quantity, produced_quantity, status,
    created_at, started_at, completed_at, deadline
)
SELECT p.id, m.id, 100, 100, 'COMPLETED',
       CURRENT_TIMESTAMP - INTERVAL '2 days',
       CURRENT_TIMESTAMP - INTERVAL '2 days',
       CURRENT_TIMESTAMP - INTERVAL '1 day',
       CURRENT_DATE - 1
FROM tb_product p
CROSS JOIN tb_machine m
WHERE p.name = 'Conjunto de válvula industrial'
  AND m.name = 'Torno CNC 01';

INSERT INTO tb_production_order (
    product_id, machine_id, target_quantity, produced_quantity, status,
    created_at, started_at, completed_at, deadline
)
SELECT p.id, m.id, 50, 0, 'PENDING',
       CURRENT_TIMESTAMP, NULL, NULL, CURRENT_DATE + 7
FROM tb_product p
CROSS JOIN tb_machine m
WHERE p.name = 'Eixo de transmissão'
  AND m.name = 'Torno CNC 01';

INSERT INTO tb_production_order (
    product_id, machine_id, target_quantity, produced_quantity, status,
    created_at, started_at, completed_at, deadline
)
SELECT p.id, m.id, 80, 35, 'IN_PROGRESS',
       CURRENT_TIMESTAMP - INTERVAL '4 hours',
       CURRENT_TIMESTAMP - INTERVAL '3 hours', NULL, CURRENT_DATE + 2
FROM tb_product p
CROSS JOIN tb_machine m
WHERE p.name = 'Carcaça de bomba'
  AND m.name = 'Torno CNC 01';

INSERT INTO tb_production_record (production_order_id, quantity, registered_at, notes)
SELECT o.id, 60, CURRENT_TIMESTAMP - INTERVAL '2 days', 'Primeiro lote concluído.'
FROM tb_production_order o
JOIN tb_product p ON p.id = o.product_id
WHERE p.name = 'Conjunto de válvula industrial'
  AND o.status = 'COMPLETED';

INSERT INTO tb_production_record (production_order_id, quantity, registered_at, notes)
SELECT o.id, 40, CURRENT_TIMESTAMP - INTERVAL '1 day', 'Lote final concluído.'
FROM tb_production_order o
JOIN tb_product p ON p.id = o.product_id
WHERE p.name = 'Conjunto de válvula industrial'
  AND o.status = 'COMPLETED';

INSERT INTO tb_production_record (production_order_id, quantity, registered_at, notes)
SELECT o.id, 20, CURRENT_TIMESTAMP - INTERVAL '2 hours', 'Primeiro apontamento da ordem.'
FROM tb_production_order o
JOIN tb_product p ON p.id = o.product_id
WHERE p.name = 'Carcaça de bomba'
  AND o.status = 'IN_PROGRESS';

INSERT INTO tb_production_record (production_order_id, quantity, registered_at, notes)
SELECT o.id, 15, CURRENT_TIMESTAMP - INTERVAL '1 hour', 'Segundo apontamento da ordem.'
FROM tb_production_order o
JOIN tb_product p ON p.id = o.product_id
WHERE p.name = 'Carcaça de bomba'
  AND o.status = 'IN_PROGRESS';
