package com.centurionvigo.controller;

import com.centurionvigo.exception.ResourceNotFoundException;
import com.centurionvigo.model.Matricula;
import com.centurionvigo.repository.MatriculaRepository;
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
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaRepository repository;

    @GetMapping
    public CollectionModel<EntityModel<Matricula>> obtenerTodas() {
        List<EntityModel<Matricula>> matriculas = repository.findAll().stream()
                .map(matricula -> EntityModel.of(matricula,
                        linkTo(methodOn(MatriculaController.class).obtenerPorId(matricula.getIdMatricula())).withSelfRel(),
                        linkTo(methodOn(EstudianteController.class).obtenerPorId(matricula.getEstudiante().getIdEstudiante())).withRel("estudiante")))
                .collect(Collectors.toList());

        return CollectionModel.of(matriculas, 
                linkTo(methodOn(MatriculaController.class).obtenerTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Matricula> obtenerPorId(@PathVariable Long id) {
        Matricula matricula = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no registrada con el Id: " + id));

        return EntityModel.of(matricula,
                linkTo(methodOn(MatriculaController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(MatriculaController.class).obtenerTodas()).withRel("lista-matriculas"),
                linkTo(methodOn(EstudianteController.class).obtenerPorId(matricula.getEstudiante().getIdEstudiante())).withRel("ver-estudiante"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Matricula> crearMatricula(@RequestBody Matricula matricula) {
        Matricula nueva = repository.save(matricula);
        return EntityModel.of(nueva,
                linkTo(methodOn(MatriculaController.class).obtenerPorId(nueva.getIdMatricula())).withSelfRel());
    }

    @PutMapping("/{id}")
    public EntityModel<Matricula> actualizarMatricula(@PathVariable Long id, @RequestBody Matricula datosActualizados) {
        Matricula matricula = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Matrícula no registrada con el Id: " + id));

        matricula.setPeriodoAcademico(datosActualizados.getPeriodoAcademico());
        matricula.setFechaMatricula(datosActualizados.getFechaMatricula());
        matricula.setEstado(datosActualizados.getEstado());
        matricula.setIdCurso(datosActualizados.getIdCurso());

        Matricula guardada = repository.save(matricula);
        return EntityModel.of(guardada,
                linkTo(methodOn(MatriculaController.class).obtenerPorId(guardada.getIdMatricula())).withSelfRel());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMatricula(@PathVariable Long id) {
        Matricula matricula = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Matrícula no registrada con el Id: " + id));

        repository.delete(matricula);
        return ResponseEntity.noContent().build();
    }
}