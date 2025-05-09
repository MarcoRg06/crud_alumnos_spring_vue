package com.marco.alumnos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.marco.alumnos.model.Alumno;
import com.marco.alumnos.repository.AlumnoRepository;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
    @Autowired
    private AlumnoRepository alumnoRepository;
    //metodo get para obtener todos los alumnos de la base de datos
    @GetMapping("/traer-lumnos")
    public List<Alumno> TraerAlumnos() {
        return alumnoRepository.findAll();
        
    }
    //metodo get para insertar un alumno en la base de datos
    @PostMapping("/insertar-alumnos")
    public Alumno InsertarAlumno(@RequestBody Alumno alumno) {
        return alumnoRepository.save(alumno);
    }
    //Metodo para eidtar un alumno de la base de datos
    @PutMapping("/editar-alumnos/{id}")
    public ResponseEntity<Alumno> EditarAlumno(@PathVariable Long id,@RequestBody Alumno alumno) {
        return alumnoRepository.findById(id).map(alumnoExistente -> {
            alumnoExistente.setNombre(alumno.getNombre());
            alumnoExistente.setApellidos(alumno.getApellidos());
            alumnoExistente.setEmail(alumno.getEmail());
            alumnoExistente.setTelefono(alumno.getTelefono());
            alumnoExistente.setNumeroControl(alumno.getNumeroControl());
            alumnoExistente.setCarrera(alumno.getCarrera());
            alumnoExistente.setImagenURL(alumno.getImagenURL());
            Alumno actualizado = alumnoRepository.save(alumnoExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
