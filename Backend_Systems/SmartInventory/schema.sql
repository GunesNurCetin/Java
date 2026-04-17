-- Ürünlerin ana bilgilerini tutan tablo
CREATE TABLE products (
    product_id NUMBER PRIMARY KEY,
    product_name VARCHAR2(100) NOT NULL,
    stock_quantity NUMBER DEFAULT 0,
    base_price NUMBER(10, 2),
    current_price NUMBER(10, 2),
    min_threshold NUMBER DEFAULT 10 -- Kritik stok sınırı
);

-- Fiyat değişimlerini takip eden tarihçe tablosu
CREATE TABLE price_history (
    history_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id NUMBER,
    old_price NUMBER(10, 2),
    new_price NUMBER(10, 2),
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);
