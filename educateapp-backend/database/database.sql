CREATE DATABASE IF NOT EXISTS db_educateapp;
USE db_educateapp;

CREATE TABLE estudiantes (
    id_estudiante BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    fecha_nacimiento DATE,
    fecha_ingreso DATE,
    estado VARCHAR(20)
);

CREATE TABLE docentes (
    id_docente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    especialidad VARCHAR(100),
    estado VARCHAR(20)
);

CREATE TABLE cursos (
    id_curso BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    creditos INT,
    semestre INT,
    id_docente BIGINT,
    FOREIGN KEY (id_docente) REFERENCES docentes(id_docente) ON DELETE SET NULL
);

CREATE TABLE matriculas (
    id_matricula BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante BIGINT NOT NULL,
    id_curso BIGINT NOT NULL,
    periodo_academico VARCHAR(20),
    fecha_matricula DATE,
    estado VARCHAR(20),
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante) ON DELETE CASCADE,
    FOREIGN KEY (id_curso) REFERENCES cursos(id_curso) ON DELETE CASCADE
);

CREATE TABLE horarios (
    id_horario BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_curso BIGINT NOT NULL,
    dia_semana VARCHAR(20),
    hora_inicio TIME,
    hora_fin TIME,
    aula VARCHAR(50),
    FOREIGN KEY (id_curso) REFERENCES cursos(id_curso) ON DELETE CASCADE
);

CREATE TABLE evaluaciones (
    id_evaluacion BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_matricula BIGINT NOT NULL,
    tipo_evaluacion VARCHAR(50),
    fecha DATE,
    nota DECIMAL(4,2),
    FOREIGN KEY (id_matricula) REFERENCES matriculas(id_matricula) ON DELETE CASCADE
);