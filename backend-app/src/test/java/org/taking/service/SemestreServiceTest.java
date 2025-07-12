package org.taking.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.taking.model.Semestre;
import org.taking.repository.SemestreRepository;

import jakarta.inject.Inject;
import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SemestreServiceTest {

  @InjectMock SemestreRepository semestreRepository;
  @Inject SemestreService semestreService;

  private Semestre semestre;

  @BeforeEach
  void setUp() {
    semestre = new Semestre();
    semestre.setId(1L);
    semestre.setName("1º Semestre");
  }

  @Test
  void getSemestresOK() {
    List<Semestre> semestres = new ArrayList<>();
    semestres.add(semestre);
    Mockito.when(semestreRepository.listAll()).thenReturn(semestres);
    List<Semestre> semestres1 = semestreService.getSemestres("");
    assertFalse(semestres1.isEmpty());
    assertEquals(1L, semestres1.get(0).getId());
    assertEquals("1º Semestre", semestres1.get(0).getName());
    assertEquals(semestres, semestres1);
  }

  @Test
  void findSemestreByIdOK() {
    Mockito.when(semestreRepository.findByIdOptional(1L)).thenReturn(Optional.of(semestre));

    Optional<Semestre> semestreOptional = semestreRepository.findByIdOptional(1L);
    assertNotNull(semestreOptional);
    assertEquals(1L, semestreOptional.get().getId());
    assertEquals("1º Semestre", semestreOptional.get().getName());
  }

  @Test
  void createOK() {

    Mockito.doNothing().when(semestreRepository).persist(ArgumentMatchers.any(Semestre.class));

    Mockito.when(semestreRepository.isPersistent(ArgumentMatchers.any(Semestre.class))).thenReturn(true);

    Semestre newSemestre = new Semestre();
    newSemestre.setName("2º Semestre");
    assertDoesNotThrow(() -> semestreService.create(newSemestre));
  }

  @Test
  void createKO() {

    Mockito.doNothing().when(semestreRepository).persist(ArgumentMatchers.any(Semestre.class));

    Mockito.when(semestreRepository.isPersistent(ArgumentMatchers.any(Semestre.class))).thenReturn(true);

    Semestre newSemestre = new Semestre();
    newSemestre.setId(1L);
    newSemestre.setName("2º Semestre");
    Exception exception = assertThrows(InvalidAttributesException.class, () -> semestreService.create(newSemestre));
    assertTrue(exception.getMessage().contains("Id tem que ser nulo"));
  }
}
