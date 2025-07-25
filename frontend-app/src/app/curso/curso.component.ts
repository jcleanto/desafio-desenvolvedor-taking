import { TitleCasePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { FormBuilder, FormGroup, FormGroupDirective, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Curso } from './curso';
import { CursoService } from './curso.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface ICursoDialog {
  editingCurso: Curso;
  isEditingCurso: boolean;
  list: () => {};
}

@Component({
  selector: 'app-curso',
  imports: [
    MatTableModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './curso.component.html',
  styleUrls: ['./curso.component.css'],
  providers: [TitleCasePipe]
})
export class CursoComponent implements OnInit {
  private _snackBar = inject(MatSnackBar);

  cursos: Curso[] = [];
  displayedColumns: string[] = ['id', 'name', 'action'];
  isCreatingCurso: boolean = false;
  isEditingCurso: boolean = false;
  editingCurso!: Curso;
  errorMessage: string | null = null;
  readonly dialog = inject(MatDialog);
  
  constructor(private cursoService: CursoService, public confirmDialog: MatDialog) { }

  ngOnInit(): void {
    this.list();
  }

  openConfirmDialog(curso: Curso): void {
    const confirmDialogRef = this.confirmDialog.open(ConfirmDialogComponent, {
      data: { title: `o Curso de ${curso.name}`, delete: () => this.delete(curso) }
    });

    confirmDialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openCursoDialog(curso?: Curso): void {
    const dialogRef = this.dialog.open(CursoDialog, {
      data: {
        editingCurso: curso,
        isEditingCurso: !!curso,
        list: () => this.list()
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result !== undefined) {
        this.editingCurso.name = result;
      }
    });
  }

  list(): void {
    this.cursoService.list().subscribe(cursos => this.cursos = cursos);
  }

  delete(curso: Curso): void {
    this.cursoService.delete(curso).subscribe({
      next: (curso) => {
        this.list();
        this.errorMessage = null;
      },
      error: (error) => {
        this.errorMessage = error.message;
        this._snackBar.open(this.errorMessage || '', 'Fechar', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
        });
      }
    });
  }

}

@Component({
  selector: 'curso-dialog',
  templateUrl: 'curso.dialog.component.html',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
  ],
  providers: [TitleCasePipe]
})
export class CursoDialog {
  private _snackBar = inject(MatSnackBar);
  readonly dialogRef = inject(MatDialogRef<CursoDialog>);
  readonly data = inject<ICursoDialog>(MAT_DIALOG_DATA);

  cursoForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private cursoService: CursoService,
    private formBuilder: FormBuilder,
    private titlecasePipe: TitleCasePipe) { }

  ngOnInit(): void {
    this.cursoForm = this.formBuilder.group({
      name: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ]
    });

    if (this.data.isEditingCurso) {
      this.cursoForm.setValue({ name: this.data.editingCurso.name });
    }
  }

  createOrUpdate(formDirective: FormGroupDirective): void {
    if (this.data.isEditingCurso) {
      this.update();
    } else {
      this.create(formDirective);
    }
  }

  create(formDirective: FormGroupDirective): void {
    const curso: Curso = this.cursoForm.getRawValue() as Curso;
    curso.name = this.titlecasePipe.transform(curso.name);
    this.cursoService.create(curso).subscribe({
      next: (curso) => {
        this.data.list();
        this.errorMessage = null;
      },
      error: (error) => {
        this.errorMessage = error.message;
        this._snackBar.open(this.errorMessage || '', 'Fechar', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
        });
      }
    });
    formDirective.resetForm();
    this.cursoForm.reset();
    this.dialogRef.close();
  }

  update(): void {
    this.data.editingCurso.name = this.titlecasePipe.transform(this.cursoForm.get('name')?.value);
    this.cursoService.update(this.data.editingCurso).subscribe({
      next: (curso) => {
        this.data.list();
        this.errorMessage = null;
      },
      error: (error) => {
        this.errorMessage = error.message;
        this._snackBar.open(this.errorMessage || '', 'Fechar', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
        });
      }
    });
    this.data.isEditingCurso = false;
    this.cursoForm.reset();
    this.dialogRef.close();
  }

  edit(curso: Curso): void {
    this.data.isEditingCurso = true;
    this.data.editingCurso = curso;
    this.cursoForm.setValue({ name: this.data.editingCurso.name });
  }

  cancelEdit(): void {
    this.dialogRef.close();
  }

  getErrorMessage(field: string): string {
    if (this.cursoForm.get(field)?.hasError('required')) {
      return `Requerido`;
    }

    if (this.cursoForm.get(field)?.hasError('minlength')) {
      return `Deve ter no mínimo ${this.cursoForm.get(field)?.errors?.['minlength'].requiredLength} caracteres`;
    }

    return this.cursoForm.get(field)?.invalid ? `Campo inválido ${field}` : '';
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
