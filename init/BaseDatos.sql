DROP DATABASE `banco`;

CREATE SCHEMA `banco`;

USE `banco`;

CREATE TABLE personas (
    id_persona INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    edad INT,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(50),
    genero VARCHAR(50)
);

CREATE TABLE clientes (
    id_cliente INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    persona_id INT NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado BIT,
    FOREIGN KEY (persona_id) REFERENCES personas(id_persona)
);

CREATE TABLE cuentas (
    id_cuenta INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50),
    saldo_inicial DOUBLE PRECISION,
    estado BIT,
    cliente_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id_cliente)
);

CREATE TABLE movimientos (
    id_movimiento INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(50),
    valor DOUBLE PRECISION,
    saldo DOUBLE PRECISION,
    cuenta_id INT NOT NULL,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id_cuenta)
);
