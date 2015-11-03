-- name:create-product!
-- creates a new product
INSERT INTO products
(name, description, price)
VALUES (:name, :description, :price)

-- name:list-products
-- selects all available products
SELECT * from products