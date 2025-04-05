-- Create enum types
CREATE TYPE meal_type AS ENUM ('soup', 'meat', 'fish');
CREATE TYPE meal_time AS ENUM ('lunch', 'dinner');
CREATE TYPE reservation_status AS ENUM ('active', 'cancelled', 'used');

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
    meal_time meal_time NOT NULL,

    UNIQUE(restaurant_id, date)
);

CREATE TABLE meal (
    id BIGSERIAL PRIMARY KEY,
    menu_id BIGINT NOT NULL REFERENCES menu(id),
    description TEXT NOT NULL,
    meal_type meal_type NOT NULL
);

CREATE TABLE reservation (
    code VARCHAR(8) PRIMARY KEY,
    status reservation_status NOT NULL DEFAULT 'active',
    meal_id BIGINT NOT NULL REFERENCES meal(id)
);

INSERT INTO restaurant (name, location, capacity) VALUES
('Restaurante Grelhados', 'Universidade de Aveiro', 100),
('Cantina de Santiago', 'Universidade de Aveiro', 400),
('Cantina Castro', 'ESSUA', 200);
