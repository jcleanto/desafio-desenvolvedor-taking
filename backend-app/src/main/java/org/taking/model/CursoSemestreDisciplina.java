package org.taking.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

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

  @ManyToOne
  @MapsId("Id")
  @JoinColumn(name = "curso_id", insertable=false, updatable=false)
  private Curso curso;

  @ManyToOne
  @MapsId("Id")
  @JoinColumn(name = "semestre_id", insertable=false, updatable=false)
  private Semestre semestre;

  @ManyToOne
  @MapsId("Id")
  @JoinColumn(name = "disciplina_id", insertable=false, updatable=false)
  private Disciplina disciplina;
}
