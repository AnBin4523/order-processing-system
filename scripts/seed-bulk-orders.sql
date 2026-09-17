-- Bulk-generate fake orders for performance/pagination testing.
-- NOT a Flyway migration — run manually via psql when you need load-test data.
-- Usage:
--   docker exec -i order-processing-postgres psql -U order_user -d order_service < scripts/seed-bulk-orders.sql
-- Change the upper bound of generate_series to control row count (default: 1,000,000).

INSERT INTO orders (customer_name, customer_email, status, total_amount, currency, created_at, updated_at)
SELECT
    'Customer ' || i,
    'customer' || i || '@example.com',
    (ARRAY['PENDING', 'CONFIRMED', 'FAILED', 'CANCELLED'])[1 + floor(random() * 4)::int],
    round((random() * 5000000 + 10000)::numeric, 2),
    'VND',
    now() - (random() * interval '365 days'),
    now()
FROM generate_series(1, 1000000) AS s(i);