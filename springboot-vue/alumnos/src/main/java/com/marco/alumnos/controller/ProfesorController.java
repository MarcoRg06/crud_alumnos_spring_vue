package com.marco.alumnos.controller;
import com.marco.alumnos.model.Alumno;
import com.marco.alumnos.model.Profesor;

import com.marco.alumnos.repository.ProfesorRepository;
import com.marco.alumnos.services.AlumnoService;
import com.marco.alumnos.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;
    @GetMapping("/traer-profesor")
    public  List <Profesor> traerProfesor(){
        return profesorService.obtenerTodos();
    }
    @GetMapping("/traer-profesor/{id}")
    public ResponseEntity<Profesor> traerUnDocente (@PathVariable Long id){
        Optional<Profesor> profesor = profesorService.obtenerPorId(id);
        return profesor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/insertar-profesor")
    public Profesor insertarDocente(@RequestBody Profesor profesor){
        return profesorService.guardarProfesor(profesor);
    }
    @PutMapping("/editar-profesor/{id}")
    public ResponseEntity<Profesor> actualizarProfesor(@PathVariable Long id, @RequestBody Profesor profesor){
        Optional<Profesor> actualizado = profesorService.actualizarProfesor(id,profesor);
        return actualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping ("/eliminar-profesor/{id}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable Long id){
        profesorService.eliminarProfesor(id);
        return  ResponseEntity.ok().build();
    }
}
