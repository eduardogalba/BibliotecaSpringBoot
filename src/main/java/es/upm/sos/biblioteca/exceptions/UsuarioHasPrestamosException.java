package es.upm.sos.biblioteca.exceptions;

public class UsuarioHasPrestamosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsuarioHasPrestamosException(Integer id) {
        super("El usuario con id " + id + " tiene préstamos asociados");
    }  


}
