package org.taking.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.taking.model.Disciplina;
import org.taking.repository.DisciplinaRepository;

import jakarta.inject.Inject;
import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class DisciplinaServiceTest {

  @InjectMock DisciplinaRepository disciplinaRepository;
  @Inject DisciplinaService disciplinaService;

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
    Mockito.when(disciplinaRepository.listAll()).thenReturn(disciplinas);
    List<Disciplina> disciplinas1 = disciplinaService.getDisciplinas("");
    assertFalse(disciplinas1.isEmpty());
    assertEquals(1L, disciplinas1.get(0).getId());
    assertEquals("Disciplina 1", disciplinas1.get(0).getName());
    assertEquals(disciplinas, disciplinas1);
  }

  @Test
  void findDisciplinaByIdOK() {
    Mockito.when(disciplinaRepository.findByIdOptional(1L)).thenReturn(Optional.of(disciplina));

    Optional<Disciplina> disciplinaOptional = disciplinaRepository.findByIdOptional(1L);
    assertNotNull(disciplinaOptional);
    assertEquals(1L, disciplinaOptional.get().getId());
    assertEquals("Disciplina 1", disciplinaOptional.get().getName());
  }

  @Test
  void createOK() {

    Mockito.doNothing().when(disciplinaRepository).persist(ArgumentMatchers.any(Disciplina.class));

    Mockito.when(disciplinaRepository.isPersistent(ArgumentMatchers.any(Disciplina.class))).thenReturn(true);

    Disciplina newSemestre = new Disciplina();
    newSemestre.setName("Disciplina 2");
    assertDoesNotThrow(() -> disciplinaService.create(newSemestre));
  }

  @Test
  void createKO() {

    Mockito.doNothing().when(disciplinaRepository).persist(ArgumentMatchers.any(Disciplina.class));

    Mockito.when(disciplinaRepository.isPersistent(ArgumentMatchers.any(Disciplina.class))).thenReturn(true);

    Disciplina newDisciplina = new Disciplina();
    newDisciplina.setId(1L);
    newDisciplina.setName("Disciplina 2");
    Exception exception = assertThrows(InvalidAttributesException.class, () -> disciplinaService.create(newDisciplina));
    assertTrue(exception.getMessage().contains("Id tem que ser nulo"));
  }
}
