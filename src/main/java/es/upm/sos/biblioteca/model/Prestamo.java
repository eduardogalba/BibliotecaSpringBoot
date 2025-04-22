package es.upm.sos.biblioteca.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "prestamo")
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo extends RepresentationModel<Prestamo> {

    @EmbeddedId // Indica PrestamoId es la clave primaria de esta entidad
    private PrestamoId id;

    @ManyToOne // Cada instancia Prestamo va a tener un usuario
    @MapsId("usuarioId") // Asocia la clave primaria al campo correspondiente
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;

    @ManyToOne // Cada instancia Prestamo va a tener un libro
    @MapsId("libroId") // Asocia la clave primaria al campo correspondiente
    @JoinColumn(name = "libroId")
    private Libro libro;

    @Column(name = "fechaPrestamo")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaPrestamo;

    @Column(name = "fechaDevolucion")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaDevolucion;

    @PrePersist
    public void prePersist() {
        fechaPrestamo = LocalDateTime.now();
        fechaDevolucion = fechaPrestamo.plusDays(14);
    }
}