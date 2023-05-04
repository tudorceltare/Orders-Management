-- create the schema for the orders management application
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- create the clients table
CREATE TABLE public.clients(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL
);

-- create the products table
CREATE TABLE public.products(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price REAL NOT NULL,
    quantity INTEGER NOT NULL
);

-- create the orders table
CREATE TABLE public.orders(
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (client_id) REFERENCES public.clients(id)
);

-- create the order_products table
CREATE TABLE public.order_products(
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES public.orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES public.products(id) ON DELETE CASCADE
);

-- create the log table
CREATE TABLE public.logs(
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Insert dummy data into the clients table
INSERT INTO public.clients (name, email)
VALUES
    ('John Doe', 'johndoe@example.com'),
    ('Jane Doe', 'janedoe@example.com'),
    ('Alice Smith', 'alicesmith@example.com'),
    ('Bob Johnson', 'bobjohnson@example.com'),
    ('Eva Green', 'evagreen@example.com'),
    ('Adam Brown', 'adambrown@example.com'),
    ('Olivia White', 'oliviawhite@example.com');

-- Insert dummy data into the products table
INSERT INTO public.products (name, price, quantity)
VALUES
    ('Product 1', 10.50, 20),
    ('Product 2', 5.75, 50),
    ('Product 3', 8.25, 30),
    ('Product 4', 12.00, 15),
    ('Product 5', 15.99, 25),
    ('Product 6', 9.50, 40),
    ('Product 7', 6.25, 10);

-- Insert dummy data into the orders table
INSERT INTO public.orders (client_id, total_price)
VALUES
    (1, 26.75),
    (2, 25.50),
    (3, 27.00),
    (4, 51.25),
    (5, 24.50);

-- Insert dummy data into the order_products table
INSERT INTO order_products (order_id, product_id, quantity)
VALUES
    (1, 1, 2),
    (1, 2, 1),
    (2, 2, 3),
    (2, 3, 1),
    (3, 1, 1),
    (3, 3, 2),
    (4, 1, 3),
    (4, 2, 2),
    (4, 3, 1),
    (5, 1, 1),
    (5, 2, 1),
    (5, 3, 1);

INSERT INTO public.logs (message)
VALUES
    ('=========================================
                 BILL
=========================================
CLIENT NAME: John Doe

PRODUCTS:
Name                 Price      Quantity   Subtotal
--------------------------------------------------
Product 1            $10.50     2          $21.00
Product 2            $5.75      1          $5.75

Total:                                   $26.75    '),
    ('=========================================
                 BILL
=========================================
CLIENT NAME: Jane Doe

PRODUCTS:
Name                 Price      Quantity   Subtotal
--------------------------------------------------
Product 2            $5.75      3          $17.25
Product 3            $8.25      1          $8.25

Total:                                   $25.50    '),
    ('=========================================
                 BILL
=========================================
CLIENT NAME: Alice Smith

PRODUCTS:
Name                 Price      Quantity   Subtotal
--------------------------------------------------
Product 1            $10.50     1          $10.50
Product 3            $8.25      2          $16.50

Total:                                   $27.00    '),
    ('=========================================
                 BILL
=========================================
CLIENT NAME: Bob Johnson

PRODUCTS:
Name                 Price      Quantity   Subtotal
--------------------------------------------------
Product 1            $10.50     3          $31.50
Product 2            $5.75      2          $11.50
Product 3            $8.25      1          $8.25

Total:                                   $51.25    '),
    ('=========================================
                 BILL
=========================================
CLIENT NAME: Eva Green

PRODUCTS:
Name                 Price      Quantity   Subtotal
--------------------------------------------------
Product 1            $10.50     1          $10.50
Product 2            $5.75      1          $5.75
Product 3            $8.25      1          $8.25

Total:                                   $24.50    ');
