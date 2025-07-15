package org.taking.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.taking.model.Curso;
import org.taking.service.CursoService;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CursoControllerTest {

  @InjectMock CursoService cursoService;
  @Inject CursoController cursoController;

  private Curso curso;

  @BeforeEach
  void setUp() {
    curso = new Curso();
    curso.setId(1L);
    curso.setName("Curso 1");
  }

  @Test
  @TestSecurity(user = "administrador", roles = {"admin", "aluno"})
  void getCursosOK() {
    List<Curso> cursos = new ArrayList<>();
    cursos.add(curso);
    Mockito.when(cursoService.getCursos("")).thenReturn(cursos);
    Response response = cursoController.getCursos("");
    assertNotNull(response);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertNotNull(response.getEntity());
    List<Curso> entity = (List<Curso>) response.getEntity();
    assertFalse(entity.isEmpty());
    assertEquals(1L, entity.get(0).getId());
    assertEquals("Curso 1", entity.get(0).getName());
  }

  @Test
  @TestSecurity(user = "administrador", roles = {"admin"})
  void getByIdOK() {
    Mockito.when(cursoService.findCursoById(1L)).thenReturn(Optional.of(curso));

    Response response = cursoController.getById(1L);
    assertNotNull(response);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertNotNull(response.getEntity());
    Curso entity = (Curso) response.getEntity();
    assertEquals(1L, entity.getId());
    assertEquals("Curso 1", entity.getName());
  }

  @Test
  @TestSecurity(user = "administrador", roles = {"admin"})
  void getByIdKO() {
    Mockito.when(cursoService.findCursoById(1L)).thenReturn(Optional.empty());

    Response response = cursoController.getById(1L);
    assertNotNull(response);
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertNull(response.getEntity());
  }
}
