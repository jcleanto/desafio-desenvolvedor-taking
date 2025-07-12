import { Component, OnInit, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import {
  FormBuilder,
  FormGroup,
  FormGroupDirective,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { CursoSemestreDisciplina } from './gradecurricular';
import { CursoSemestreDisciplinaService } from './gradecurricular.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { CursoService } from '../curso/curso.service';
import { SemestreService } from '../semestre/semestre.service';
import { DisciplinaService } from '../disciplina/disciplina.service';
import { Curso } from '../curso/curso';
import { Semestre } from '../semestre/semestre';
import { Disciplina } from '../disciplina/disciplina';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

export interface IGradecurricularDialog {
  cursos: Curso[];
  semestres: Semestre[];
  disciplinas: Disciplina[];
  list: () => {};
}

@Component({
  selector: 'app-gradecurricular',
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './gradecurricular.component.html',
  styleUrl: './gradecurricular.component.css'
})
export class GradecurricularComponent implements OnInit {

  cursoSemestreDisciplinas: CursoSemestreDisciplina[] = [];
  cursos: Curso[] = [];
  semestres: Semestre[] = [];
  disciplinas: Disciplina[] = [];
  displayedColumns: string[] = ['curso_name', 'semestre_name', 'disciplina_name', 'action'];
  readonly dialog = inject(MatDialog);

  constructor(
    private cursoSemestreDisciplinaService: CursoSemestreDisciplinaService,
    private cursoService: CursoService,
    private semestreService: SemestreService,
    private disciplinaService: DisciplinaService,
    public confirmDialog: MatDialog) { }

  ngOnInit(): void {
    this.list();
    this.listCursos();
    this.listSemestres();
    this.listDisciplinas();
  }

  openConfirmDialog(cursoSemestreDisciplina: CursoSemestreDisciplina): void {
    const confirmDialogRef = this.confirmDialog.open(ConfirmDialogComponent, {
      data: { title: `a Grade Curricular de ${cursoSemestreDisciplina.curso.name} - ${cursoSemestreDisciplina.semestre.name} - ${cursoSemestreDisciplina.disciplina.name}`, delete: () => this.delete(cursoSemestreDisciplina) }
    });

    confirmDialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openGradecurricularDialog(): void {
    const dialogRef = this.dialog.open(GradecurricularDialog, {
      data: {
        cursos: this.cursos,
        semestres: this.semestres,
        disciplinas: this.disciplinas,
        list: () => this.list()
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  list(): void {
    this.cursoSemestreDisciplinaService.list().subscribe(cursoSemestreDisciplinas => this.cursoSemestreDisciplinas = cursoSemestreDisciplinas);
  }

  listCursos(): void {
    this.cursoService.list().subscribe(cursos => this.cursos = cursos);
  }

  listSemestres(): void {
    this.semestreService.list().subscribe(semestres => this.semestres = semestres);
  }

  listDisciplinas(): void {
    this.disciplinaService.list().subscribe(disciplinas => this.disciplinas = disciplinas);
  }

  delete(disciplina: CursoSemestreDisciplina): void {
    this.cursoSemestreDisciplinaService.delete(disciplina).subscribe(disciplina => this.list());
  }

}

@Component({
  selector: 'gradecurricular-dialog',
  templateUrl: 'gradecurricular.dialog.component.html',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
  ]
})
export class GradecurricularDialog {
  readonly dialogRef = inject(MatDialogRef<GradecurricularDialog>);
  readonly data = inject<IGradecurricularDialog>(MAT_DIALOG_DATA);

  gradeCurricularForm!: FormGroup;

  constructor(
    private cursoSemestreDisciplinaService: CursoSemestreDisciplinaService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.gradeCurricularForm = this.formBuilder.group({
      curso: [null, [Validators.required]],
      semestre: [null, [Validators.required]],
      disciplina: [null, [Validators.required]]
    });
  }

  create(formDirective: FormGroupDirective): void {
    // console.log('', this.gradeCurricularForm.getRawValue());
    const cursoSemestreDisciplina: CursoSemestreDisciplina = this.gradeCurricularForm.getRawValue() as CursoSemestreDisciplina;
    this.cursoSemestreDisciplinaService.create(cursoSemestreDisciplina).subscribe(cursoSemestreDisciplina => this.data.list());
    formDirective.resetForm();
    this.gradeCurricularForm.reset();
    this.dialogRef.close();
  }

  cancel(): void {
    this.dialogRef.close();
  }

  getErrorMessage(field: string): string {
    if (this.gradeCurricularForm.get(field)?.hasError('required')) {
      return `Requerido`;
    }

    return this.gradeCurricularForm.get(field)?.invalid ? `Campo inválido ${field}` : '';
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
