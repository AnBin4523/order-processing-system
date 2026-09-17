ALTER TABLE products ADD COLUMN image_url VARCHAR(500);

UPDATE products SET image_url = 'https://picsum.photos/seed/' || id || '/400/300';