package es.upm.sos.biblioteca.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Integer id) {
        super("Empleado con id "+id+" no encontrado.");
      }
}

