package org.taking.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.taking.model.Curso;
import org.taking.repository.CursoRepository;

import jakarta.inject.Inject;
import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CursoServiceTest {

  @InjectMock CursoRepository cursoRepository;
  @Inject CursoService cursoService;

  private Curso curso;

  @BeforeEach
  void setUp() {
    curso = new Curso();
    curso.setId(1L);
    curso.setName("Curso 1");
  }

  @Test
  void getCursosOK() {
    List<Curso> cursos = new ArrayList<>();
    cursos.add(curso);
    Mockito.when(cursoRepository.listAll()).thenReturn(cursos);
    List<Curso> cursos1 = cursoService.getCursos("");
    assertFalse(cursos1.isEmpty());
    assertEquals(1L, cursos1.get(0).getId());
    assertEquals("Curso 1", cursos1.get(0).getName());
    assertEquals(cursos, cursos1);
  }

  @Test
  void findCursoByIdOK() {
    Mockito.when(cursoRepository.findByIdOptional(1L)).thenReturn(Optional.of(curso));

    Optional<Curso> cursoOptional = cursoRepository.findByIdOptional(1L);
    assertNotNull(cursoOptional);
    assertEquals(1L, cursoOptional.get().getId());
    assertEquals("Curso 1", cursoOptional.get().getName());
  }

  @Test
  void createOK() {

    Mockito.doNothing().when(cursoRepository).persist(ArgumentMatchers.any(Curso.class));

    Mockito.when(cursoRepository.isPersistent(ArgumentMatchers.any(Curso.class))).thenReturn(true);

    Curso newCurso = new Curso();
    newCurso.setName("Curso 2");
    assertDoesNotThrow(() -> cursoService.create(newCurso));
  }

  @Test
  void createKO() {

    Mockito.doNothing().when(cursoRepository).persist(ArgumentMatchers.any(Curso.class));

    Mockito.when(cursoRepository.isPersistent(ArgumentMatchers.any(Curso.class))).thenReturn(true);

    Curso newCurso = new Curso();
    newCurso.setId(1L);
    newCurso.setName("Curso 2");
    Exception exception = assertThrows(InvalidAttributesException.class, () -> cursoService.create(newCurso));
    assertTrue(exception.getMessage().contains("Id tem que ser nulo"));
  }
}
