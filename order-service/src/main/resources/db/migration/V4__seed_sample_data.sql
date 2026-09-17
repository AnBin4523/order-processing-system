INSERT INTO products (name, price, currency) VALUES
    ('Ao thun cotton nam', 159000, 'VND'),
    ('Quan jean nu', 359000, 'VND'),
    ('Giay sneaker trang', 799000, 'VND'),
    ('Balo du lich', 529000, 'VND'),
    ('Tai nghe bluetooth', 399000, 'VND'),
    ('Dong ho deo tay', 1250000, 'VND');

INSERT INTO orders (customer_name, customer_email, status, total_amount, currency) VALUES
    ('Nguyen Van Minh', 'minh.nguyen89@gmail.com', 'CONFIRMED', 1117000, 'VND'),
    ('Le Thi Huong', 'huong.le92@gmail.com', 'PENDING', 888000, 'VND'),
    ('Pham Quoc Bao', 'quocbao.pham@gmail.com', 'CANCELLED', 399000, 'VND'),
    ('Vo Thi Ngoc Anh', 'ngocanh.vo@gmail.com', 'CONFIRMED', 1727000, 'VND');

-- Nguyen Van Minh: 2x Ao thun cotton nam, 1x Giay sneaker trang
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 2, p.price
FROM orders o, products p
WHERE o.customer_email = 'minh.nguyen89@gmail.com' AND p.name = 'Ao thun cotton nam';

INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 1, p.price
FROM orders o, products p
WHERE o.customer_email = 'minh.nguyen89@gmail.com' AND p.name = 'Giay sneaker trang';

-- Le Thi Huong: 1x Quan jean nu, 1x Balo du lich
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 1, p.price
FROM orders o, products p
WHERE o.customer_email = 'huong.le92@gmail.com' AND p.name = 'Quan jean nu';

INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 1, p.price
FROM orders o, products p
WHERE o.customer_email = 'huong.le92@gmail.com' AND p.name = 'Balo du lich';

-- Pham Quoc Bao: 1x Tai nghe bluetooth
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 1, p.price
FROM orders o, products p
WHERE o.customer_email = 'quocbao.pham@gmail.com' AND p.name = 'Tai nghe bluetooth';

-- Vo Thi Ngoc Anh: 1x Dong ho deo tay, 3x Ao thun cotton nam
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 1, p.price
FROM orders o, products p
WHERE o.customer_email = 'ngocanh.vo@gmail.com' AND p.name = 'Dong ho deo tay';

INSERT INTO order_items (order_id, product_id, quantity, unit_price)
SELECT o.id, p.id, 3, p.price
FROM orders o, products p
WHERE o.customer_email = 'ngocanh.vo@gmail.com' AND p.name = 'Ao thun cotton nam';
