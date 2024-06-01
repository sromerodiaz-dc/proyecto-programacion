-- Crear la tabla enemies
CREATE TABLE enemies (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    who INTEGER NOT NULL,
    sentido CHAR(1) NOT NULL,
    speed INTEGER NOT NULL,
    max_life INTEGER NOT NULL,
    life INTEGER NOT NULL,
    height INTEGER NOT NULL,
    width INTEGER NOT NULL,
    solid_area_x INTEGER NOT NULL,
    solid_area_y INTEGER NOT NULL,
    solid_area_width INTEGER NOT NULL,
    solid_area_height INTEGER NOT NULL,
    default_solid_area_x INTEGER NOT NULL,
    default_solid_area_y INTEGER NOT NULL,
    intervalo INTEGER NOT NULL
);

-- Insertar un registro para el objeto Viello
INSERT INTO enemies (who, id, sentido, speed, max_life, life, height, width, solid_area_x, solid_area_y, solid_area_width, solid_area_height, default_solid_area_x, default_solid_area_y, intervalo)
VALUES (2, 'Viello_feroz', '1', 2, 30, 30, 50, 40, 15, 30, 30, 30, 15, 30, 20);

-- Insertar un registro para el objeto Dinoseto
INSERT INTO enemies (who, id, sentido, speed, max_life, life, height, width, solid_area_x, solid_area_y, solid_area_width, solid_area_height, default_solid_area_x, default_solid_area_y, intervalo)
VALUES (2, 'Dinoseto_elegante', '0', 1, 20, 20, 40, 35, 10, 25, 25, 25, 10, 25, 15);
