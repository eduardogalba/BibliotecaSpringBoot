package es.upm.sos.biblioteca.exceptions;

public class LibroNotAllowedException extends RuntimeException {
  public LibroNotAllowedException() {
    super("Error la edad del empleado tiene que ser mayor que 16.");
  }
}
