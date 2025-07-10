package org.taking.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.taking.model.Curso;
import org.taking.model.CursoSemestreDisciplina;
import org.taking.model.Disciplina;
import org.taking.model.Semestre;
import org.taking.service.CursoSemestreDisciplinaService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CursoSemestreDisciplinaControllerTest {

  @InjectMock
  CursoSemestreDisciplinaService cursoSemestreDisciplinaService;
  @Inject CursoSemestreDisciplinaController cursoSemestreDisciplinaController;

  private Curso curso;
  private Semestre semestre;
  private Disciplina disciplina;
  private CursoSemestreDisciplina cursoSemestreDisciplina;

  @BeforeEach
  void setUp() {
    curso = new Curso();
    curso.setId(1L);
    curso.setName("Curso 1");
    semestre = new Semestre();
    semestre.setId(1L);
    semestre.setName("1º Semestre");
    disciplina = new Disciplina();
    disciplina.setId(1L);
    disciplina.setName("Disciplina 1");
    cursoSemestreDisciplina = new CursoSemestreDisciplina();
    cursoSemestreDisciplina.setCurso(curso);
    cursoSemestreDisciplina.setSemestre(semestre);
    cursoSemestreDisciplina.setDisciplina(disciplina);
  }

  @Test
  void getCursoSemestreDisciplinasOK() {
    List<CursoSemestreDisciplina> cursoSemestreDisciplinas = new ArrayList<>();
    cursoSemestreDisciplinas.add(cursoSemestreDisciplina);
    Mockito.when(cursoSemestreDisciplinaService.getCursoSemestreDisciplinas()).thenReturn(cursoSemestreDisciplinas);
    Response response = cursoSemestreDisciplinaController.getCursoSemestreDisciplinas();
    assertNotNull(response);
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertNotNull(response.getEntity());
    List<CursoSemestreDisciplina> entity = (List<CursoSemestreDisciplina>) response.getEntity();
    assertFalse(entity.isEmpty());
    assertEquals(curso, entity.get(0).getCurso());
    assertEquals(semestre, entity.get(0).getSemestre());
    assertEquals(disciplina, entity.get(0).getDisciplina());
  }
}
