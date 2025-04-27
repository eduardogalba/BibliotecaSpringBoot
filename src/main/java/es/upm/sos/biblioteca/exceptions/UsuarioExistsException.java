package es.upm.sos.biblioteca.exceptions;

public class UsuarioExistsException extends RuntimeException {
  public UsuarioExistsException(String correo) {
    super("Usuario con correo " + correo + " ya existe.");
  }
}
