package es.upm.sos.biblioteca.exceptions;

public class LibroNotFoundException extends RuntimeException {
  public LibroNotFoundException(Integer id) {
    super(String.format("Libro con id %s no encontrado.", id));
  }
}
