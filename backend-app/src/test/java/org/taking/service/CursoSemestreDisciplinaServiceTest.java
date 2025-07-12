package org.taking.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.taking.model.Curso;
import org.taking.model.CursoSemestreDisciplina;
import org.taking.model.Disciplina;
import org.taking.model.Semestre;
import org.taking.repository.CursoSemestreDisciplinaRepository;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CursoSemestreDisciplinaServiceTest {

  @InjectMock
  CursoSemestreDisciplinaRepository cursoSemestreDisciplinaRepository;
  @Inject CursoSemestreDisciplinaService cursoSemestreDisciplinaService;

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
    Mockito.when(cursoSemestreDisciplinaRepository.listAll()).thenReturn(cursoSemestreDisciplinas);
    List<CursoSemestreDisciplina> cursoSemestreDisciplinas1 = cursoSemestreDisciplinaService.getCursoSemestreDisciplinas();
    assertFalse(cursoSemestreDisciplinas1.isEmpty());
    assertEquals(curso, cursoSemestreDisciplinas1.get(0).getCurso());
    assertEquals(semestre, cursoSemestreDisciplinas1.get(0).getSemestre());
    assertEquals(disciplina, cursoSemestreDisciplinas1.get(0).getDisciplina());
    assertEquals(cursoSemestreDisciplinas, cursoSemestreDisciplinas1);
  }
}
