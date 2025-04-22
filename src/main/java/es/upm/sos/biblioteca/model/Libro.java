package es.upm.sos.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "libro") // Necesario para indicar el nombre de la tabla en la base de datos
@Data // Indica a Lombok crear los métodosgetter, setter, equals(), hashCode(), y
// toString()
@NoArgsConstructor // Crea un constructor vacío
@AllArgsConstructor // Crea un constructor con todos los campos
@JsonIgnoreProperties(ignoreUnknown = true) // Permite validar el json recibido de tal manera que
											// tenga los mismo campos que aparecen en el recurso
public class Libro extends RepresentationModel<Libro> {

	@Id
	@Column(name = "libroId")
	@GeneratedValue(strategy = GenerationType.IDENTITY) // valor generado por la base de datos
	@Schema(description = "Id del Libro", required = false, example = "1")
	private int libroId;

	@Column(name = "titulo")
	@Schema(description = "Titulo del libro", required = true, example = "Alicia en el país de las maravillas")
	@NotNull(message = "El titulo es obligatorio y no puede ser null")
	private String titulo;

	@Column(name = "autores")
	@Schema(description = "Autores del libro", required = true, example = "Lewis Carroll")
	@NotNull(message = "El autor es obligatorio y no puede ser null")
	private String autores;

	
	@Column(name = "edicion")
	@Schema(description = "Edición del libro", required = true, example = "Nº 1")
	@NotNull(message = "La edición es obligatoria y no puede ser null")
	private String edicion;

	@Column(name = "isbn")
	@Schema(description = "ISBN del libro", required = true, example = "978-8415618713")
	@NotNull(message = "El isbn es obligatorio y no puede ser null")
	@Size(min = 10, max = 13, message = "El isbn debe tener entre 10 y 13 caracteres")
	private String isbn;

	@Column(name = "editorial")
	@Schema(description = "Editorial del libro", required = true, example = "Editorial Alma")
	@NotNull(message = "La editorial es obligatoria y no puede ser null")
	private String editorial;

	@Column(name = "volumenes")
	@Schema(description = "Número de undidades del libro", required = true, example = "1")
	@NotNull(message = "El número de volumenes es obligatorio y no puede ser null")
	private int volumenes;

	@Column(name = "prestados")
	@Schema(description = "Número de libros prestados", required = false, example = "0")
	private int prestados;
}
