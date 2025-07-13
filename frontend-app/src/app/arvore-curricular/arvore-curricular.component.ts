import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTreeModule } from '@angular/material/tree';
import { Curso } from '../curso/curso';
import { CursoSemestreDisciplinaService } from '../gradecurricular/gradecurricular.service';
import { CursoService } from '../curso/curso.service';
import { CursoSemestreDisciplina } from '../gradecurricular/gradecurricular';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

/**
 * Dados da Arvore Curricular com estrutura aninhada.
 * Cada nó(node) tem as propriedades name e uma lista children como opcional.
 */
interface IArvoreCurricularNode {
  name: string;
  children?: IArvoreCurricularNode[];
}

@Component({
  selector: 'app-arvore-curricular',
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTreeModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
  ],
  templateUrl: './arvore-curricular.component.html',
  styleUrl: './arvore-curricular.component.css'
})
export class ArvoreCurricularComponent {

  cursos: Curso[] = [];
  cursoSemestreDisciplinas: CursoSemestreDisciplina[] = [];
  cursoSemestresDisciplinasWithIds: any[] = [];
  selectedCursoId?: number | null;
  arvoreCurricularDataSource: IArvoreCurricularNode[] = [];

  constructor(
    private cursoSemestreDisciplinaService: CursoSemestreDisciplinaService,
    private cursoService: CursoService) { }

  ngOnInit(): void {
    this.listCursos();
  }

  childrenAccessor = (node: IArvoreCurricularNode) => node.children ?? [];

  hasChild = (_: number, node: IArvoreCurricularNode) => !!node.children && node.children.length > 0;

  listCursos(): void {
    this.cursoService.list().subscribe(cursos => this.cursos = cursos);
  }

  onChangeCurso(): void {
    this.list(this.selectedCursoId);
  }

  list(cursoId?: number | null): void {
    if (cursoId) {
      this.cursoSemestreDisciplinaService.listByCurso(cursoId).subscribe(cursoSemestreDisciplinas => {
        this.cursoSemestreDisciplinas = cursoSemestreDisciplinas;
        // monta primeiro uma estrutura básica de IArvoreCurricularNode sem agrupamento
        this.cursoSemestresDisciplinasWithIds = this.cursoSemestreDisciplinas.map((cursoSemestreDisciplina) => {
          return {
              name: cursoSemestreDisciplina.curso.name,
              children: [
                {
                  name: cursoSemestreDisciplina.semestre.name,
                  children: [
                    {
                      name: cursoSemestreDisciplina.disciplina.name,
                    }
                  ]
                }
              ],
            };
        });
        // monta a estrutura final de IArvoreCurricularNode agrupando por name de cada nó(curso, semestre e disciplina) da árvore curricular
        this.arvoreCurricularDataSource = [...new Set(this.cursoSemestresDisciplinasWithIds.map(e => e.name))]
          .map(cursoName => {
            const semestres = [...new Set(this.cursoSemestresDisciplinasWithIds
                  .filter(p => p.name === cursoName)
                  .map(s => (s.children)).flat(1))];
            return { 
              name: cursoName, 
              children: [...new Set(semestres.map(s => s.name))]
                    .map(semestreName => ({
                      name: semestreName,
                      children: semestres.filter(s => s.name === semestreName).map(d => (d.children)).flat(1)
                    })),
            };
          });

      });
    } else {
      this.cursoSemestreDisciplinaService.list().subscribe(cursoSemestreDisciplinas => this.cursoSemestreDisciplinas = cursoSemestreDisciplinas);
    }
  }

}
