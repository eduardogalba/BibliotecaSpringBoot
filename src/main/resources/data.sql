-- Insertar usuarios
INSERT INTO usuario (nombre, matricula, nacimiento, correo) VALUES
('Juan Pérez', 'Z12345', '1992-06-15', 'juan.perez@upm.es'),
('Ana Gómez', 'Z67890', '2001-11-22', 'ana.gomez@alumnos.upm.es'),
('Carlos López', 'Z54321', '2003-04-10', 'carlos.lopez@alumnos.upm.es'),
('Laura Martínez', 'Z98765', '1995-08-12', 'laura.martinez@upm.es'),
('Pedro Sánchez', 'Z54322', '1998-02-25', 'pedro.sanchez@alumnos.upm.es'),
('María Fernández', 'Z87654', '2000-12-05', 'maria.fernandez@alumnos.upm.es'),
('Luis García', 'Z65432', '1997-07-19', 'luis.garcia@upm.es'),
('Sofía Torres', 'Z32109', '2002-03-30', 'sofia.torres@alumnos.upm.es')
ON CONFLICT DO NOTHING;

-- Insertar libros
INSERT INTO libro (titulo, autores, edicion, isbn, editorial, volumenes, prestados) VALUES
('El Quijote', 'Miguel de Cervantes', '1ra', '978-3-16-148410-0', 'Editorial Clásicos', 2, 0),
('Cien Años de Soledad', 'Gabriel García Márquez', '2da', '978-84-376-0494-7', 'Sudamericana', 3, 0),
('1984', 'George Orwell', '3ra', '978-0-452-28423-4', 'Secker & Warburg', 4, 0),
('El Principito', 'Antoine de Saint-Exupéry', '3ra', '978-0-15-601219-5', 'Reynal & Hitchcock', 2, 0),
('La Sombra del Viento', 'Carlos Ruiz Zafón', '1ra', '978-84-08-03720-9', 'Planeta', 1, 0),
('Crónica de una Muerte Anunciada', 'Gabriel García Márquez', '1ra', '978-84-376-0494-9', 'Sudamericana', 3, 0),
('Fahrenheit 451', 'Ray Bradbury', '4ta', '978-0-7432-4722-1', 'Ballantine Books', 2, 0)
ON CONFLICT DO NOTHING;

