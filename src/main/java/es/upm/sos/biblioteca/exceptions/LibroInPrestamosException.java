package es.upm.sos.biblioteca.exceptions;

public class LibroInPrestamosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LibroInPrestamosException(Integer id) {
        super("El libro con id " + id + " tiene préstamos asociados");
    }

}
