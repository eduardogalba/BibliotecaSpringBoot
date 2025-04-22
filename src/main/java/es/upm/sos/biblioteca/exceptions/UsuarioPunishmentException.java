package es.upm.sos.biblioteca.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioPunishmentException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public UsuarioPunishmentException(int usuarioId, LocalDateTime fechaFinSancion) {
        super("El usuario con id " + usuarioId + " tiene una sanción pendiente hasta " + fechaFinSancion.format(formatter));
    }

}
