import { Component } from '@angular/core';
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
interface ArvoreCurricularNode {
  name: string;
  children?: ArvoreCurricularNode[];
}

@Component({
  selector: 'app-arvore-curricular',
  imports: [
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
  arvoreCurricularDataSource: ArvoreCurricularNode[] = [];

  constructor(
    private cursoSemestreDisciplinaService: CursoSemestreDisciplinaService,
    private cursoService: CursoService) { }

  ngOnInit(): void {
    this.listCursos();
  }

  childrenAccessor = (node: ArvoreCurricularNode) => node.children ?? [];

  hasChild = (_: number, node: ArvoreCurricularNode) => !!node.children && node.children.length > 0;

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
        console.log('this.cursoSemestreDisciplinas', this.cursoSemestreDisciplinas);
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
        this.arvoreCurricularDataSource = this.cursoSemestresDisciplinasWithIds.map(({children, ...r}: any) => (
          {
            ...r,
            children: children.map(({children, ...keep}: any) => (
              {
                ...keep,
                children
              }
            ))
          }
        ));
        console.log('this.arvoreCurricularDataSource', this.arvoreCurricularDataSource);
        /*
        // fazendo o agrupamento por curso
        this.cursoSemestresDisciplinasWithIds = this.cursoSemestresDisciplinasWithIds.reduce(function(acc, item) {
          var category = item.name;
          if (!acc[category]) {
            acc[category] = [];
          }
          acc[category].push(item.children);
          return acc;
        }, []);
        */

      });
    } else {
      this.cursoSemestreDisciplinaService.list().subscribe(cursoSemestreDisciplinas => this.cursoSemestreDisciplinas = cursoSemestreDisciplinas);
    }
  }

}
