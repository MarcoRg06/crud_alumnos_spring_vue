package com.marco.alumnos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.marco.alumnos.model.Alumno;

public interface AlumnoRepository  extends JpaRepository<Alumno, Long>{


}