-- Create enum types
CREATE TYPE meal_type AS ENUM ('SOUP', 'MEAT', 'FISH');
CREATE TYPE menu_time AS ENUM ('LUNCH', 'DINNER');
CREATE TYPE reservation_status AS ENUM ('ACTIVE', 'CANCELLED', 'USED');

CREATE TABLE restaurant (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL
);

CREATE TABLE menu (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurant(id),
    date DATE NOT NULL,
    time VARCHAR(6) NOT NULL,
    capacity INTEGER NOT NULL,

    UNIQUE(restaurant_id, date, time)
);

CREATE TABLE meal (
    id BIGSERIAL PRIMARY KEY,
    menu_id BIGINT NOT NULL REFERENCES menu(id),
    description TEXT NOT NULL,
    type VARCHAR(4) NOT NULL,
    UNIQUE(menu_id, type)
);

CREATE TABLE reservation (
    code VARCHAR(8) PRIMARY KEY,
    status VARCHAR(9) NOT NULL DEFAULT 'ACTIVE',
    meal_id BIGINT NOT NULL REFERENCES meal(id)
);

INSERT INTO restaurant (name, location, capacity) VALUES
('Restaurante Grelhados', 'Universidade de Aveiro', 100),
('Cantina de Santiago', 'Universidade de Aveiro', 400),
('Cantina Castro', 'ESSUA', 200);
