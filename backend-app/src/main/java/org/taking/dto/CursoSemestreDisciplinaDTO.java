package org.taking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoSemestreDisciplinaDTO {

  private CursoDTO curso;
  private SemestreDTO semestre;
  private DisciplinaDTO disciplina;

}
