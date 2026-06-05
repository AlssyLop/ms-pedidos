CREATE DATABASE IF NOT EXISTS plazoleta_pedidos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE plazoleta_pedidos;

CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    nombre_cliente VARCHAR(100) NOT NULL,
    celular VARCHAR(13) NOT NULL,
    id_restaurante BIGINT NOT NULL,
    estado ENUM('PENDIENTE', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    id_empleado BIGINT NULL,
    pin VARCHAR(6) NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_pedido_restaurante_estado (id_restaurante, estado),
    INDEX idx_pedido_cliente (id_cliente)
) ENGINE=InnoDB;

CREATE TABLE detalle_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_plato BIGINT NOT NULL,
    nombre_plato VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    UNIQUE (id_pedido, id_plato),
    INDEX idx_detalle_pedido (id_pedido)
) ENGINE=InnoDB;
