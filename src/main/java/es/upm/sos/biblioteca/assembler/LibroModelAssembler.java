package es.upm.sos.biblioteca.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.upm.sos.biblioteca.controller.LibrosController;
import es.upm.sos.biblioteca.model.Libro;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for
 * Pagination.
 * It converts the Customer Entity to the Customer Model and has the code for it
 */
@Component
public class LibroModelAssembler extends RepresentationModelAssemblerSupport<Libro, Libro> {
    public LibroModelAssembler() {
        super(LibrosController.class, Libro.class);
    }

    @Override
    public Libro toModel(Libro entity) {

        entity.add(linkTo(methodOn(LibrosController.class).getLibro(entity.getLibroId())).withSelfRel());

        return entity;
    }
}
