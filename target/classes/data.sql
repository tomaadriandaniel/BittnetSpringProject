-- === USERS ===
INSERT INTO shop_user(id, username, password, firstname, surname, city, number, street, zipcode) VALUES
(1, 'admin', 'admin', 'Ana', 'Popescu', 'Bucuresti', 1, 'Aviatorilor', '123456'),
(2, 'client', 'client', 'Alex', 'Marin', 'Bucuresti', 25, 'Victoriei', '321654'),
(3, 'expeditor', 'expeditor', 'Mihai', 'Ionescu', 'Cluj', 45, 'Eroilor', '400123'),
(4, 'editor', 'editor', 'Edit', 'Or', 'Brasov', 12, 'Corectorului', '500500');

-- === USER ROLES ===
INSERT INTO user_roles(user_id, roles) VALUES
(1, 'ADMIN'),
(1, 'EXPEDITOR'),
(2, 'CLIENT'),
(3, 'EXPEDITOR'),
(4, 'EDITOR');

-- === PRODUCTS ===
INSERT INTO product(id, code, currency, description, price, stock, valid) VALUES
(1, 'P001', 'EUR', 'Smartphone', 599.99, 10, true),
(2, 'P002', 'USD', 'Laptop', 999.99, 5, true),
(3, 'P003', 'RON', 'Headphones', 149.99, 25, true),
(4, 'P004', 'EUR', 'Keyboard', 89.99, 30, true);

-- === ORDERS (for client ID 2) ===
INSERT INTO online_order(id, user_id, delivered, returned, cancelled, total_price) VALUES
(1, 2, false, false, false, 1199.98),
(2, 2, true, false, false, 149.99),
(3, 2, false, false, true, 89.99);

-- === ORDER ITEMS ===
INSERT INTO online_order_item(id, product_id, quantity, order_id) VALUES
(1, 1, 2, 1), -- 2x Smartphone
(2, 3, 1, 2), -- 1x Headphones
(3, 4, 1, 3); -- 1x Keyboard
