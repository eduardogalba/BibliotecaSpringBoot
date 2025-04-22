package es.upm.sos.biblioteca.exceptions;

import es.upm.sos.biblioteca.model.PrestamoId;

public class PrestamoOutOfDateException extends RuntimeException {

    public PrestamoOutOfDateException(PrestamoId id) {
        super("El préstamo del usuario " + id.getUsuarioId() + " y el libro " + id.getLibroId() + " está fuera de fecha");
    }

}
