UPDATE products
SET image_url = 'https://placehold.co/400x300?text=' || replace(name, ' ', '+');
