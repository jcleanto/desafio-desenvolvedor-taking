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
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  FormBuilder,
  FormGroup,
  FormGroupDirective,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Disciplina } from './disciplina';
import { DisciplinaService } from './disciplina.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

export interface IDisciplinaDialog {
  editingDisciplina: Disciplina;
  isEditingDisciplina: boolean;
  list: () => {};
}

@Component({
  selector: 'app-disciplina',
  imports: [
    MatTableModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './disciplina.component.html',
  styleUrl: './disciplina.component.css',
  providers: [TitleCasePipe]
})
export class DisciplinaComponent implements OnInit {
  private _snackBar = inject(MatSnackBar);

  disciplinas: Disciplina[] = [];
  displayedColumns: string[] = ['id', 'name', 'action'];
  isEditingDisciplina: boolean = false;
  editingDisciplina!: Disciplina;
  errorMessage: string | null = null;
  readonly dialog = inject(MatDialog);

  constructor(private disciplinaService: DisciplinaService, public confirmDialog: MatDialog) { }

  ngOnInit(): void {
    this.list();
  }

  openConfirmDialog(disciplina: Disciplina): void {
    const confirmDialogRef = this.confirmDialog.open(ConfirmDialogComponent, {
      data: { title: `a Disciplina ${disciplina.name}`, delete: () => this.delete(disciplina) }
    });

    confirmDialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openDisciplinaDialog(disciplina?: Disciplina): void {
    const dialogRef = this.dialog.open(DisciplinaDialog, {
      data: {
        editingDisciplina: disciplina,
        isEditingDisciplina: !!disciplina,
        list: () => this.list()
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result !== undefined) {
        this.editingDisciplina.name = result;
      }
    });
  }

  list(): void {
    this.disciplinaService.list().subscribe(disciplinas => this.disciplinas = disciplinas);
  }

  delete(disciplina: Disciplina): void {
    this.disciplinaService.delete(disciplina).subscribe({
      next: (disciplina) => {
        this.list();
        this.errorMessage = null; // Clear any previous error
      },
      error: (error) => {
        this.errorMessage = error.message; // Display the error message to the user
        // You can also perform other actions based on the error
        this._snackBar.open(`Delete primeiro as referências a essa Disciplina na Grade Curricular ${error.status}`, 'Fechar', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
        });
      }
    });

    // this.disciplinaService.delete(disciplina).subscribe(disciplina => this.list());
  }

}

@Component({
  selector: 'disciplina-dialog',
  templateUrl: 'disciplina.dialog.component.html',
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
export class DisciplinaDialog {
  readonly dialogRef = inject(MatDialogRef<DisciplinaDialog>);
  readonly data = inject<IDisciplinaDialog>(MAT_DIALOG_DATA);

  disciplinaForm!: FormGroup;

  constructor(
    private disciplinaService: DisciplinaService,
    private formBuilder: FormBuilder,
    private titlecasePipe: TitleCasePipe) { }

  ngOnInit(): void {
    this.disciplinaForm = this.formBuilder.group({
      name: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ]
    });

    if (this.data.isEditingDisciplina) {
      this.disciplinaForm.setValue({ name: this.data.editingDisciplina.name });
    }
  }

  createOrUpdate(formDirective: FormGroupDirective): void {
    if (this.data.isEditingDisciplina) {
      this.update();
    } else {
      this.create(formDirective);
    }
  }

  create(formDirective: FormGroupDirective): void {
    const disciplina: Disciplina = this.disciplinaForm.getRawValue() as Disciplina;
    disciplina.name = this.titlecasePipe.transform(disciplina.name);
    this.disciplinaService.create(disciplina).subscribe(disciplina => this.data.list());
    formDirective.resetForm();
    this.disciplinaForm.reset();
    this.dialogRef.close();
  }

  update(): void {
    this.data.editingDisciplina.name = this.titlecasePipe.transform(this.disciplinaForm.get('name')?.value);
    this.disciplinaService.update(this.data.editingDisciplina).subscribe(disciplina => this.data.list());
    this.data.isEditingDisciplina = false;
    this.disciplinaForm.reset();
    this.dialogRef.close();
  }

  edit(disciplina: Disciplina): void {
    this.data.isEditingDisciplina = true;
    this.data.editingDisciplina = disciplina;
    this.disciplinaForm.setValue({ name: this.data.editingDisciplina.name });
  }

  cancelEdit(): void {
    this.dialogRef.close();
  }

  getErrorMessage(field: string): string {
    if (this.disciplinaForm.get(field)?.hasError('required')) {
      return `Requerido`;
    }

    if (this.disciplinaForm.get(field)?.hasError('minlength')) {
      return `Deve ter no mínimo ${this.disciplinaForm.get(field)?.errors?.['minlength'].requiredLength} caracteres`;
    }

    return this.disciplinaForm.get(field)?.invalid ? `Campo inválido ${field}` : '';
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
