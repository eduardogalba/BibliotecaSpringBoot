package es.upm.sos.biblioteca.model;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data // Indica a Lombok crear los métodosgetter, setter, equals(), hashCode(), y
// toString()
@NoArgsConstructor // Crea un constructor vacío
@AllArgsConstructor // Crea un constructor con todos los campos
@EqualsAndHashCode(callSuper = false)
public class Usuario extends RepresentationModel<Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioId")
    private int usuarioId;
    @NotNull(message = "El nombre es obligatorio para poder identificar al usuario")
    private String nombre;

    @Column(name = "matricula")
    @Pattern(regexp="^[A-Z]\\d{6}$", message = "La matrícula debe empezar por letra mayúscula seguida de 6 dígitos")
    private String matricula;

    @Column(name = "nacimiento")
    @NotNull(message = "El teléfono es obligatorio para las comunicaciones")
    private Date nacimiento;

    @Column(name = "correo")
    @Pattern(regexp="^[a-zA-Z]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?@(alumnos|fi)\\.upm\\.es$", message = "El correo debe ser una dirección de correo válida de la Escuela")
    @NotNull(message = "El correo es obligatorio para las comunicaciones")
    private String correo;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL) // Solo mostrar si no es null
    private Set<EntityModel<Libro>> prestamos;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL) // Solo mostrar si no es null
    private Set<EntityModel<Historico>> devueltos;
}
