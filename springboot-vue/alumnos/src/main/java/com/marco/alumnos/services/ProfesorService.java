package com.marco.alumnos.services;

import com.marco.alumnos.model.Alumno;
import com.marco.alumnos.model.Profesor;
import com.marco.alumnos.repository.AlumnoRepository;
import com.marco.alumnos.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorService  {
    @Autowired

    private ProfesorRepository profesorRepository;

    public List<Profesor> obtenerTodos(){
        return profesorRepository.findAll();
    }
    public Optional<Profesor> obtenerPorId(Long id){
        return profesorRepository.findById(id);
    }
    public Profesor guardarProfesor(Profesor profesor){
        return profesorRepository.save(profesor);
    }

    public Optional<Profesor> actualizarProfesor (Long id, Profesor profesorDetalles){
        return profesorRepository.findById(id).map(profesorExistene ->{
            profesorExistene.setNombre(profesorDetalles.getNombre());
            profesorExistene.setApellido(profesorDetalles.getApellido());
            profesorExistene.setEmail(profesorDetalles.getEmail());
            profesorExistene.setRfc(profesorDetalles.getRfc());
            profesorExistene.setTelefono(profesorDetalles.getTelefono());
            profesorExistene.setMateria(profesorDetalles.getMateria());
            profesorExistene.setImagenURL(profesorDetalles.getImagenURL());
            return profesorRepository.save(profesorDetalles);
        });
    }
    public void eliminarProfesor(Long id){
        profesorRepository.deleteById(id);
    }
}
