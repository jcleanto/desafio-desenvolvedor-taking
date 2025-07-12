package org.taking.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curso_semestre_disciplina")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CursoSemestreDisciplina {

  @EmbeddedId
  private CursoSemestreDisciplinaKey cursoSemestreDisciplinaKey;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("cursoId")
  @JoinColumn(name = "curso_id")
  private Curso curso;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("semestreId")
  @JoinColumn(name = "semestre_id")
  private Semestre semestre;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("disciplinaId")
  @JoinColumn(name = "disciplina_id")
  private Disciplina disciplina;
}
