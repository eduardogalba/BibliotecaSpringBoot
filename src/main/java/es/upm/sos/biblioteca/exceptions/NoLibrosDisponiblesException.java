package es.upm.sos.biblioteca.exceptions;

public class NoLibrosDisponiblesException extends RuntimeException {

    public NoLibrosDisponiblesException() {
        super("No hay libros disponibles en la biblioteca.");
    }

}
