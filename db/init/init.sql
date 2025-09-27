-- =========================
-- CREACIÓN DE BASE DE DATOS
-- =========================
CREATE DATABASE IF NOT EXISTS usuario;
CREATE DATABASE IF NOT EXISTS prestamos;

-- Dar privilegios al usuario appuser
GRANT ALL PRIVILEGES ON usuario.* TO 'appuser'@'%';
GRANT ALL PRIVILEGES ON prestamos.* TO 'appuser'@'%';
FLUSH PRIVILEGES;

-- =========================
-- USUARIO (micro autenticación)
-- =========================
USE usuario;

CREATE TABLE IF NOT EXISTS rol (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre` (`nombre`)
);

CREATE TABLE IF NOT EXISTS usuario (
  `idusuario` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `correoElectronico` varchar(100) NOT NULL,
  `fechaNacimiento` datetime NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `salarioBase` bigint NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `documento_identidad` varchar(50) NOT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `id_rol` int DEFAULT '2',
  `intentos` int DEFAULT '0',
  PRIMARY KEY (`idusuario`),
  KEY `fk_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
);

-- Insertar roles
INSERT INTO rol (nombre,descripcion) VALUES
('admin','Administrador del sistema'),
('user','Usuario estándar'),
('asesor','asesoría y soporte a usuarios');

-- Insertar usuarios
INSERT INTO usuario 
(nombre, apellido, correoElectronico, fechaNacimiento, direccion, salarioBase, telefono, documento_identidad, contrasena, id_rol, intentos)
VALUES
('Rafael', 'Pérez', 'admin@test.com', '1985-03-10 00:00:00', 'Calle 123', 5000000, '3001234567', '1001', '$2a$10$cKu7y.Y3JUsLBsmJhkC3huDmINHWssxyCFHh36ldcwzG/w2zqzxQ2', 1, 0),
('ana', 'garcia', 'ana.garcia1@example.com', '1990-07-21 00:00:00', 'Carrera 45', 2000000, '3012345678', '1002', '$2a$10$cKu7y.Y3JUsLBsmJhkC3huDmINHWssxyCFHh36ldcwzG/w2zqzxQ2', 2, 0),
('Luis', 'Rodríguez', 'prueba@test.com', '1992-11-05 00:00:00', 'Avenida 9', 3000000, '3023456789', '1003', '$2a$10$cKu7y.Y3JUsLBsmJhkC3huDmINHWssxyCFHh36ldcwzG/w2zqzxQ2', 3, 0);

-- =========================
-- PRÉSTAMOS (micro solicitudes)
-- =========================
USE prestamos;

CREATE TABLE IF NOT EXISTS estados (
    id_estado BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tipo_prestamo (
    id_tipo_prestamo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    monto_minimo DECIMAL(15,2) NOT NULL,
    monto_maximo DECIMAL(15,2) NOT NULL,
    tasa_interes DECIMAL(5,2) NOT NULL,
    validacion_automatica TINYINT(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS solicitud (
    id_solicitud BIGINT AUTO_INCREMENT PRIMARY KEY,
    monto DECIMAL(15,2) NOT NULL,
    plazo INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    id_estado BIGINT NOT NULL DEFAULT 1,
    id_tipo_prestamo BIGINT NOT NULL,
    identificacion VARCHAR(50),
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado),
    FOREIGN KEY (id_tipo_prestamo) REFERENCES tipo_prestamo(id_tipo_prestamo)
);

-- Insertar estados
INSERT INTO estados (nombre, descripcion) VALUES
('PENDIENTE', 'La solicitud fue aprobada exitosamente'),
('APROBADA', 'La solicitud fue registrada y está en proceso de revisión'),
('RECHAZADA', 'La solicitud fue rechazada');

-- Insertar tipos de préstamo
INSERT INTO tipo_prestamo (nombre, monto_minimo, monto_maximo, tasa_interes, validacion_automatica) VALUES
('Préstamo Personal', 500000.00, 50000000.00, 2.50, 1),
('Préstamo Libre Inversión', 1000000.00, 100000000.00, 2.00, 0),
('Préstamo Hipotecario', 20000000.00, 500000000.00, 1.50, 0),
('Préstamo Vehicular', 5000000.00, 200000000.00, 1.80, 1),
('Préstamo Empresarial', 10000000.00, 1000000000.00, 2.20, 0),
('Préstamo Educativo', 500000.00, 50000000.00, 1.50, 1);

-- Insertar solicitudes
INSERT INTO solicitud (monto, plazo, email, id_estado, id_tipo_prestamo, identificacion) VALUES
(1500000.00, 12, 'prueba@test.com', 1, 1, '1002'),
(5000000.00, 24, 'prueba@test.com', 1, 2, '1002'), 
(25000000.00, 120, 'prueba@test.com', 1, 3, '1002'),
(8000000.00, 36, 'prueba@test.com', 1, 4, '1002'); 
