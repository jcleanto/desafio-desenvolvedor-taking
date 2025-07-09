import { Curso } from "../curso/curso";
import { Disciplina } from "../disciplina/disciplina";
import { Semestre } from "../semestre/semestre";

export interface CursoSemestreDisciplina {
  curso: Curso;
  semestre: Semestre;
  disciplina: Disciplina;
}