package org.taking.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.taking.model.Disciplina;
import org.taking.service.DisciplinaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestSecurity(user = "administrador", roles = {"admin"})
public class DisciplinaControllerTest {

  @InjectMock DisciplinaService disciplinaService;
  @Inject DiscipinaController disciplinaController;

  private Disciplina disciplina;

  @BeforeEach
  void setUp() {
    disciplina = new Disciplina();
    disciplina.setId(1L);
    disciplina.setName("Disciplina 1");
  }

  @Test
  void getDisciplinasOK() {
    List<Disciplina> disciplinas = new ArrayList<>();
    disciplinas.add(disciplina);
    Mockito.when(disciplinaService.getDisciplinas("")).thenReturn(disciplinas);
    Response response = disciplinaController.getDisciplinas("");
    assertNotNull(response);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertNotNull(response.getEntity());
    List<Disciplina> entity = (List<Disciplina>) response.getEntity();
    assertFalse(entity.isEmpty());
    assertEquals(1L, entity.get(0).getId());
    assertEquals("Disciplina 1", entity.get(0).getName());
  }

  @Test
  void getByIdOK() {
    Mockito.when(disciplinaService.findDisciplinaById(1L)).thenReturn(Optional.of(disciplina));

    Response response = disciplinaController.getById(1L);
    assertNotNull(response);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertNotNull(response.getEntity());
    Disciplina entity = (Disciplina) response.getEntity();
    assertEquals(1L, entity.getId());
    assertEquals("Disciplina 1", entity.getName());
  }

  @Test
  void getByIdKO() {
    Mockito.when(disciplinaService.findDisciplinaById(1L)).thenReturn(Optional.empty());

    Response response = disciplinaController.getById(1L);
    assertNotNull(response);
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertNull(response.getEntity());
  }
}
