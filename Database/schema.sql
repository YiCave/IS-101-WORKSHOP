
CREATE TABLE consumers (
  consumer_id   INT            PRIMARY KEY,        -- manual control (no AUTO_INCREMENT)
  first_name    VARCHAR(30),
  last_name     VARCHAR(30),
  email         VARCHAR(100),                   
  password      VARCHAR(255),
  created_at    DATETIME,
  is_management BOOLEAN        DEFAULT FALSE       -- sensible default (most users aren't management)
);

CREATE TABLE items (
  product_id       INT          PRIMARY KEY,       -- manual control (no AUTO_INCREMENT)
  item_name        VARCHAR(100) UNIQUE,            -- unique product names 
  item_description VARCHAR(150),
  item_price       DECIMAL(6,2),
  quantity_left    INT,
  restock_at       DATETIME
);

CREATE TABLE transactions (
  transaction_id INT AUTO_INCREMENT PRIMARY KEY,
  consumer_id    INT,
  product_id     INT NOT NULL,
  quantity       INT,
 
  CONSTRAINT fk_transactions_consumer 
    FOREIGN KEY (consumer_id) REFERENCES consumers(consumer_id),  
  CONSTRAINT fk_transactions_product
    FOREIGN KEY (product_id)  REFERENCES items(product_id)
);

-- pre insert some datas for items
INSERT INTO items (product_id, item_name, item_description, item_price, quantity_left, restock_at) VALUES
  (101, 'Chips',   'Potato chips 60g',         2.50, 3, NULL),
  (102, 'Milo',    'Milo can 240ml',           3.20, 3, NULL),
  (103, 'Pens',    'Blue ballpoint pen',       1.50, 3, NULL),
  (104, 'Biscuit', 'Cream crackers 150g',      2.80, 3, NULL),
  (105, 'Bread',   'White bread loaf (small)', 4.00, 3, NULL);
