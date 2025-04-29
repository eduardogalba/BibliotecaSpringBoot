package es.upm.sos.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable // Indica que esta clase no es una entidad, sino que se incrusta en otra entidad
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoId {
    @NotNull(message = "Es necesario el id del usuario")
    @Column(name = "usuarioId")
    private Integer usuarioId;

    @NotNull(message = "Es necesario el id del libro")
    @Column(name = "libroId")
    private Integer libroId;
}