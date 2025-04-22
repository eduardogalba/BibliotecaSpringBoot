package es.upm.sos.biblioteca.exceptions;

public class UsuarioNotAllowedException extends RuntimeException {
    public UsuarioNotAllowedException() {
        super("Error la edad del empleado tiene que ser mayor que 16.");
      }
}

