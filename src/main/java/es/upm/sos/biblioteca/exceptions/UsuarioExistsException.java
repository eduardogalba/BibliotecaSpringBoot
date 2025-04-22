package es.upm.sos.biblioteca.exceptions;

public class UsuarioExistsException extends RuntimeException {
  public UsuarioExistsException(String nombre) {
    super("Empleado con nombre " + nombre + " ya existe.");
  }
}
