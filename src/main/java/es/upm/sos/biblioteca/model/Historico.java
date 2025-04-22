package es.upm.sos.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "historico_prestamo") // Necesario para indicar el nombre de la tabla en la base de datos
@Data // Indica a Lombok crear los métodos getter, setter, equals(), hashCode(), y toString()
@NoArgsConstructor // Crea un constructor vacío
@AllArgsConstructor // Crea un constructor con todos los campos
@JsonIgnoreProperties(ignoreUnknown = true) // Permite validar el json recibido de tal manera que tenga los mismos campos que aparecen en el recurso
public class Historico extends RepresentationModel<Historico> {

    @Id
    @Column(name = "historicoId")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // valor generado por la base de datos
    private int historicoId;

    @Column(name = "usuarioId")
    private int usuarioId;

    @Column(name = "tituloLibro")
    private String tituloLibro;

    @Column(name = "isbnLibro")
    private String isbnLibro;

    @Column(name = "fechaPrestamo")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaPrestamo;

    @Column(name = "fechaDevolucion")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaDevolucion;

    @Column(name = "devuelto")
    private Boolean devuelto;

    @PrePersist
    public void prePersist() {
        fechaPrestamo = LocalDateTime.now();
        fechaDevolucion = fechaPrestamo.plusDays(14);
    }
}