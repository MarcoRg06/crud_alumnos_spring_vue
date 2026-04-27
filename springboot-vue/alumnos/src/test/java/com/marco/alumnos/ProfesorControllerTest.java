package com.marco.alumnos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marco.alumnos.controller.ProfesorController;
import com.marco.alumnos.model.Profesor;
import com.marco.alumnos.repository.ProfesorRepository;
import com.marco.alumnos.services.ProfesorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfesorController.class)
public class ProfesorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfesorService profesorService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void debeTraerTodosLosProfesores() throws Exception {
        Profesor profesor1 = new Profesor();
        profesor1.setId(1L);
        profesor1.setNombre("Israel");
        profesor1.setEmail("Israelpop123@gmail.com");

        Profesor profesor2 = new Profesor();
        profesor2.setId(2L);
        profesor2.setNombre("Irvin");
        profesor2.setEmail("irvin@gmail.com");

        when(profesorService.obtenerTodos())
                .thenReturn(Arrays.asList(profesor1, profesor2));

        mockMvc.perform(get("/profesores/traer-profesor")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Israel")));
    }

    @Test
    public void debeInsertarUnDocente() throws Exception {
        Profesor docenteNuevo = new Profesor();
        docenteNuevo.setNombre("Pedro");
        docenteNuevo.setMateria("Desarrollo Agil");

        when(profesorService.guardarProfesor(org.mockito.ArgumentMatchers.any(Profesor.class))).thenReturn(docenteNuevo);

        mockMvc.perform(post("/profesores/insertar-profesor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docenteNuevo)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Pedro")));
        //.andExpect(jsonPath("$.nombre", is("Harry")));
    }

    @Test
    public void debeEliminarUnDocente() throws Exception {

        Long idParaEliminar = 1L;

        mockMvc.perform(delete("/profesores/eliminar-profesor/{id}", idParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(profesorService, times(1)).eliminarProfesor(idParaEliminar);

    }

    @Test
    public void debEditarUnProfesor() throws Exception {

        Long idParaEditar = 1L;

        Profesor profesorEditado = new Profesor();
        profesorEditado.setId(idParaEditar);
        profesorEditado.setNombre("Irvin editado");
        profesorEditado.setMateria("Informatica");

        when(profesorService.actualizarProfesor(eq(idParaEditar), any(Profesor.class)))
                .thenReturn(Optional.of(profesorEditado));

        mockMvc.perform(put("/profesores/editar-profesor/{id}", idParaEditar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesorEditado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Irvin editado"));

        verify(profesorService, times(1))
                .actualizarProfesor(eq(idParaEditar), any(Profesor.class));
    }
}