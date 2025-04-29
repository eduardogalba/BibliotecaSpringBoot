package es.upm.sos.biblioteca.exceptions;

import es.upm.sos.biblioteca.model.PrestamoId;

public class PrestamoAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PrestamoAlreadyExistException(PrestamoId prestamo) {
        super("Existe un préstamo con usuarioId: " + prestamo.getUsuarioId() + " y libroId: " + prestamo.getLibroId());
    }



}
