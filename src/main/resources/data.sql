-- 1) Autoparts
INSERT INTO autopart (id, name, description, price) VALUES
                                                        (1, 'Spark Plug',      'NGK Iridium IX Spark Plug',          12.99),
                                                        (2, 'Oil Filter',      'Mobil 1 Extended Performance',       9.99),
                                                        (3, 'Brake Pads',      'Power Stop Z23 Evolution Ceramic',   58.99),
                                                        (4, 'Air Filter',      'K&N High Performance Air Filter',    42.50),
                                                        (5, 'Battery',         'Optima RedTop Starting Battery',    249.99),
                                                        (6, 'Headlight Bulb',  'Sylvania SilverStar Ultra',         34.99),
                                                        (7, 'Windshield Wipers','Bosch Icon Wiper Blades',           38.99),
                                                        (8, 'Engine Oil',      'Castrol EDGE 5W-30 Full Synthetic', 39.99),
                                                        (9, 'Coolant',         'Prestone Antifreeze/Coolant',        22.99);


-- 2) Categories & Subcategories
INSERT INTO category (id, name, description, parent_category_id) VALUES
                                                                     (1, 'Brakes',      'Brake system components',      NULL),
                                                                     (2, 'Engine',      'Engine and drivetrain parts',   NULL),
                                                                     (3, 'Body',        'Body and exterior parts',       NULL),
                                                                     (4, 'Filters',     'Oil, air and fuel filters',     NULL),
                                                                     (5, 'Electrical',  'Electrical / electronics',      NULL),
                                                                     (6, 'Oil Filters', 'Engine oil filters',            4),
                                                                     (7, 'Air Filters', 'Engine air filters',            4);


-- 3) category_autopart join table
INSERT INTO category_autopart (category_id, autopart_id) VALUES
                                                             -- Brakes
                                                             (1,1),(1,2),(1,3),
                                                             -- Engine
                                                             (2,4),(2,5),(2,6),
                                                             -- Body
                                                             (3,7),(3,8),(3,9),
                                                             -- Filters
                                                             (4,3),(4,5),
                                                             -- Electrical
                                                             (5,2),(5,8);


-- 4) Orders
INSERT INTO orders (id, name, description, order_status, created_at) VALUES
                                                                         (1, 'Order #1001', 'First customer order',  'Created',    CURRENT_TIMESTAMP),
                                                                         (2, 'Order #1002', 'Second customer order', 'Processing', CURRENT_TIMESTAMP),
                                                                         (3, 'Order #1003', 'Third customer order',  'Shipped',    CURRENT_TIMESTAMP);


-- 5) Order Items
INSERT INTO order_item (id, quantity, discount, order_id, autopart_id) VALUES
                                                                           -- initial 9 items
                                                                           (1,  4,  0.0, 1, 1),
                                                                           (2,  1,  0.0, 1, 2),
                                                                           (3,  2,  5.0, 1, 3),
                                                                           (4,  1,  0.0, 2, 4),
                                                                           (5,  1, 10.0, 2, 5),
                                                                           (6,  2,  0.0, 2, 6),
                                                                           (7,  1,  0.0, 3, 7),
                                                                           (8,  4, 15.0, 3, 8),
                                                                           (9,  2,  0.0, 3, 9),
                                                                           -- additional items
                                                                           (40, 3,  0.0, 1, 4),
                                                                           (80, 3,  0.0, 2, 7),
                                                                           (120,3,  0.0, 3, 1);


-- 6) Inventory Items
INSERT INTO inventory_item (id, quantity, location_code, autopart_id) VALUES
                                                                          (1,  10, 'A1-01', 1),
                                                                          (2,   5, 'A1-02', 1),
                                                                          (3,  20, 'B2-01', 2),
                                                                          (4,   7, 'C3-05', 3),
                                                                          (5,  15, 'D4-04', 4),
                                                                          (6,   8, 'E5-02', 5),
                                                                          (7,  12, 'F6-03', 6),
                                                                          (8,  30, 'G7-01', 7),
                                                                          (9,  25, 'H8-02', 8),
                                                                          (10, 50, 'I9-05', 9),
                                                                          (11,  3, 'B2-02', 2),
                                                                          (12,  6, 'C3-06', 3),
                                                                          (13, 10, 'D4-07', 4);



-- 8) Payments
INSERT INTO payment (id, amount, payment_method, payment_date, order_id) VALUES
                                                                             (1,   50.00, 'Credit Card',   CURRENT_TIMESTAMP, 1),
                                                                             (2,   25.00, 'PayPal',        CURRENT_TIMESTAMP, 1),
                                                                             (3,   98.75, 'Bank Transfer', CURRENT_TIMESTAMP, 2),
                                                                             (4,  150.00, 'Credit Card',   CURRENT_TIMESTAMP, 2),
                                                                             (5,   80.00, 'Cash',          CURRENT_TIMESTAMP, 3);


-- 9) Shipments and Carriers
INSERT INTO shipment (id, shipment_date, tracking_number, shipment_status, order_id) VALUES
                                                                                         (1, '2025-06-01T10:15:00', 'TRACK123456', 'Shipped',    1),
                                                                                         (2, '2025-06-02T14:30:00', 'TRACK789012', 'Processing', 2),
                                                                                         (3, '2025-06-03T09:00:00', 'TRACK345678', 'Delivered',  3);

INSERT INTO shipment_carriers (shipment_id, carriers) VALUES
                                                          (1, 'DHL'),
                                                          (1, 'FedEx'),
                                                          (2, 'UPS'),
                                                          (3, 'USPS'),
                                                          (3, 'DHL');


-- 10) Persons & Addresses
INSERT INTO person (id, name, email, phone) VALUES
                                                (1, 'Alice Smith',    'alice@example.com',  '555-0101'),
                                                (2, 'Bob Johnson',    'bob@example.com',    '555-0202'),
                                                (3, 'Carol Williams', 'carol@example.com',  '555-0303'),
                                                (4, 'David Brown',    'david@example.com',  '555-0404');

INSERT INTO address (id, country, city, street, postal_code, house_number, apartment_number, person_id) VALUES
                                                                                                            (1, 'USA',     'New York', '5th Ave',       '10001', '1A',   '101', 1),
                                                                                                            (2, 'USA',     'New York', 'Madison Ave',   '10010', '42',   '8B',  1),
                                                                                                            (3, 'Canada',  'Toronto',  'Queen St',      'M5H2M9','100',  NULL, 2),
                                                                                                            (4, 'UK',      'London',   'Baker St',      'NW16XE','221B', NULL, 3),
                                                                                                            (5, 'Germany', 'Berlin',   'Unter den Linden','10117','5',   '2',  4);


-- 11) Roles

-- 11a) CustomerRole + paymentMethods
INSERT INTO customer_role (id, person_id) VALUES
                                              (1, 1),
                                              (2, 2);

INSERT INTO customer_role_payment_methods (customer_role_id, payment_method) VALUES
                                                                                 (1, 'Credit Card'),
                                                                                 (1, 'PayPal'),
                                                                                 (2, 'Bank Transfer');

-- 11b) EmployeeRole
INSERT INTO employee_role (id, position, hire_date, person_id) VALUES
                                                                   (1, 'Sales Manager',   '2020-04-15T09:00:00', 3),
                                                                   (2, 'Inventory Clerk', '2021-08-01T08:30:00', 4);

-- 11c) SupplierRole
INSERT INTO supplier_role (id, company_name, supplier_code, person_id) VALUES
                                                                           (1, 'ACME Parts Co.', 'ACME001', 2),
                                                                           (2, 'Global Motors', 'GLOB002', 4);

-- 7) Carts and Cart Items
INSERT INTO cart (id, customer_id) VALUES
                                       (1, 1),
                                       (2, 2);

INSERT INTO cart_item (id, quantity, cart_id, autopart_id) VALUES
                                                               (1, 2, 1, 1),  -- 2× Spark Plug
                                                               (2, 1, 1, 2),  -- 1× Oil Filter
                                                               (3, 4, 1, 3),  -- 4× Brake Pads
                                                               (4, 3, 2, 8),  -- 3× Engine Oil
                                                               (5, 2, 2, 9);  -- 2× Coolant
