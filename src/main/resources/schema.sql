CREATE TABLE  IF NOT EXISTS usuario (
    usuarioId SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    matricula VARCHAR(7),
    nacimiento DATE NOT NULL,
    correo VARCHAR(50) NOT NULL
);

CREATE TABLE  IF NOT EXISTS libro (
    libroId SERIAL PRIMARY KEY,
    titulo TEXT NOT NULL, 
    autores TEXT NOT NULL,
    edicion TEXT NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    editorial TEXT NOT NULL,
    volumenes INT NOT NULL,
    prestados INT NOT NULL DEFAULT 0
);

CREATE TABLE  IF NOT EXISTS prestamo (
    usuarioId INT NOT NULL,
    libroId INT NOT NULL,
    fechaPrestamo TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaDevolucion TIMESTAMP NOT NULL,
    PRIMARY KEY (usuarioId, libroId),
    FOREIGN KEY (usuarioId) REFERENCES usuario (usuarioId) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (libroId) REFERENCES libro (libroId) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE  IF NOT EXISTS historico_prestamo (
    historicoId SERIAL PRIMARY KEY,
    usuarioId INT,
    tituloLibro TEXT,
    isbnLibro TEXT,
    fechaPrestamo TIMESTAMP NOT NULL,
    fechaDevolucion TIMESTAMP NOT NULL,
    devuelto BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuarioId) REFERENCES usuario (usuarioId) ON DELETE CASCADE
);
