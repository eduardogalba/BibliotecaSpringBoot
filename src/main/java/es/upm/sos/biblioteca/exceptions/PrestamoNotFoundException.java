package es.upm.sos.biblioteca.exceptions;

public class PrestamoNotFoundException extends RuntimeException {

    public PrestamoNotFoundException(int usuarioId, int libroId) {
        super("No se ha encontrado el préstamo con usuarioId: " + usuarioId + " y libroId: " + libroId);
    }

}
