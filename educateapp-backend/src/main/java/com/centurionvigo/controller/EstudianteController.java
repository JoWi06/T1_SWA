package com.centurionvigo.controller;

import com.centurionvigo.exception.ResourceNotFoundException;
import com.centurionvigo.model.Estudiante;
import com.centurionvigo.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepository repository;

    @GetMapping
    public CollectionModel<EntityModel<Estudiante>> obtenerTodos() {
        List<EntityModel<Estudiante>> estudiantes = repository.findAll().stream()
                .map(estudiante -> EntityModel.of(estudiante,
                        linkTo(methodOn(EstudianteController.class).obtenerPorId(estudiante.getIdEstudiante())).withSelfRel(),
                        linkTo(methodOn(EstudianteController.class).eliminarEstudiante(estudiante.getIdEstudiante())).withRel("eliminar")))
                .collect(Collectors.toList());

        return CollectionModel.of(estudiantes, 
                linkTo(methodOn(EstudianteController.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Estudiante> obtenerPorId(@PathVariable Long id) {
        Estudiante estudiante = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no registrado con el Id: " + id));

        return EntityModel.of(estudiante,
                linkTo(methodOn(EstudianteController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(EstudianteController.class).obtenerTodos()).withRel("lista-estudiantes"),
                linkTo(methodOn(EstudianteController.class).eliminarEstudiante(id)).withRel("eliminar"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        Estudiante nuevo = repository.save(estudiante);
        return EntityModel.of(nuevo,
                linkTo(methodOn(EstudianteController.class).obtenerPorId(nuevo.getIdEstudiante())).withSelfRel());
    }

    @PutMapping("/{id}")
    public EntityModel<Estudiante> actualizarEstudiante(@PathVariable Long id, @RequestBody Estudiante datosActualizados) {
        Estudiante estudiante = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Estudiante no registrado con el Id: " + id));

        estudiante.setNombre(datosActualizados.getNombre());
        estudiante.setApellido(datosActualizados.getApellido());
        estudiante.setDni(datosActualizados.getDni());
        estudiante.setCorreo(datosActualizados.getCorreo());
        estudiante.setTelefono(datosActualizados.getTelefono());
        estudiante.setDireccion(datosActualizados.getDireccion());
        estudiante.setEstado(datosActualizados.getEstado());

        Estudiante guardado = repository.save(estudiante);
        return EntityModel.of(guardado,
                linkTo(methodOn(EstudianteController.class).obtenerPorId(guardado.getIdEstudiante())).withSelfRel());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        Estudiante estudiante = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Estudiante no registrado con el Id: " + id));

        repository.delete(estudiante);
        return ResponseEntity.noContent().build();
    }
}