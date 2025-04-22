package es.upm.sos.biblioteca.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.upm.sos.biblioteca.assembler.LibroModelAssembler;
import es.upm.sos.biblioteca.exceptions.LibroExistsException;
import es.upm.sos.biblioteca.exceptions.LibroNotFoundException;
import es.upm.sos.biblioteca.model.Libro;
import es.upm.sos.biblioteca.service.LibroService;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@RestController // Define un controlador REST
@RequestMapping("/libros") // Todos los endpoints empiezan por usuarios
@XmlRootElement // Para indicar que soportamos xml (opcional)
@AllArgsConstructor
public class LibrosController {

        private final LibroService service;
        // Necesarios para navegabiliad
        private PagedResourcesAssembler<Libro> pagedResourcesAssembler;
        private LibroModelAssembler libroModelAssembler;

        @PostMapping(value = "", consumes = { "application/json" })
        public ResponseEntity<Void> nuevoLibro(@Valid @RequestBody Libro nuevoLibro) {
                if (!service.buscarPorISBN(nuevoLibro.getIsbn())) {
                        Libro libro = service.crearLibro(nuevoLibro);

                        return ResponseEntity.created(linkTo(LibrosController.class).slash(libro.getLibroId()).toUri())
                                        .build();
                }
                throw new LibroExistsException(nuevoLibro);
        }

        @GetMapping(value = "/{id}", produces = { "application/json"})
        public ResponseEntity<Libro> getLibro(@PathVariable Integer id) {

                Libro libro = service.buscarPorId(id)
                                .orElseThrow(() -> new LibroNotFoundException(id));
                libro.add(linkTo(methodOn(LibrosController.class).getLibro(id)).withSelfRel());
                return ResponseEntity.ok(libro);
        }

        @GetMapping(value = "", produces = { "application/json"})
        public ResponseEntity<PagedModel<Libro>> filtered(
                        @RequestParam(defaultValue = "", required = false) String pattern,
                        @RequestParam(defaultValue = "false", required = false) boolean disponible,
                        @RequestParam(defaultValue = "0", required = true) int page,
                        @RequestParam(defaultValue = "2", required = true) int size) {

                Page<Libro> libros = service.buscarLibros(pattern, disponible, page, size);

                // fetch the page object by additionally passing paginable with the filters
                return ResponseEntity.ok(pagedResourcesAssembler.toModel(libros, libroModelAssembler));
        }

        @PutMapping(value ="/{id}", consumes = { "application/json" })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public ResponseEntity<Void> replaceLibro(@RequestBody Libro newLibro, @PathVariable Integer id) {
                service.buscarPorId(id)
                                .map(libro -> {
                                        libro.setTitulo(newLibro.getTitulo());
                                        libro.setAutores(newLibro.getAutores());
                                        libro.setEdicion(newLibro.getEdicion());
                                        libro.setIsbn(newLibro.getIsbn());
                                        libro.setEditorial(newLibro.getEditorial());
                                        return service.crearLibro(libro);
                                })
                                .orElseThrow(() -> new LibroNotFoundException(id));
                return ResponseEntity.noContent().build();
        }

        @DeleteMapping(value = "/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public ResponseEntity<Void> deleteLibro(@PathVariable Integer id) {
                if (service.existeLibroPorId(id)) {
                        service.eliminarLibro(id);
                } else {
                        throw new LibroNotFoundException(id);
                }
                return ResponseEntity.noContent().build();
        }

}
