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
import {
  FormBuilder,
  FormGroup,
  FormGroupDirective,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Semestre } from './semestre';
import { SemestreService } from './semestre.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface ISemestreDialog {
  editingSemestre: Semestre;
  isEditingSemestre: boolean;
  list: () => {};
}

@Component({
  selector: 'app-semestre',
  imports: [
    MatTableModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './semestre.component.html',
  styleUrl: './semestre.component.css',
  providers: [TitleCasePipe]
})
export class SemestreComponent implements OnInit {
  private _snackBar = inject(MatSnackBar);

  semestres: Semestre[] = [];
  displayedColumns: string[] = ['id', 'name', 'action'];
  isEditingSemestre: boolean = false;
  editingSemestre!: Semestre;
  errorMessage: string | null = null;
  readonly dialog = inject(MatDialog);

  constructor(private semestreService: SemestreService, public confirmDialog: MatDialog) { }

  ngOnInit(): void {
    this.list();
  }

  openConfirmDialog(semestre: Semestre): void {
    const confirmDialogRef = this.confirmDialog.open(ConfirmDialogComponent, {
      data: { title: `o Semestre ${semestre.name}`, delete: () => this.delete(semestre) }
    });

    confirmDialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openSemestreDialog(semestre?: Semestre): void {
    const dialogRef = this.dialog.open(SemestreDialog, {
      data: {
        editingSemestre: semestre,
        isEditingSemestre: !!semestre,
        list: () => this.list()
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result !== undefined) {
        this.editingSemestre.name = result;
      }
    });
  }

  list(): void {
    this.semestreService.list().subscribe(semestres => this.semestres = semestres);
  }

  delete(semestre: Semestre): void {
    this.semestreService.delete(semestre).subscribe({
      next: (semestre) => {
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
  selector: 'semestre-dialog',
  templateUrl: 'semestre.dialog.component.html',
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
export class SemestreDialog {
  private _snackBar = inject(MatSnackBar);
  readonly dialogRef = inject(MatDialogRef<SemestreDialog>);
  readonly data = inject<ISemestreDialog>(MAT_DIALOG_DATA);

  semestreForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private semestreService: SemestreService,
    private formBuilder: FormBuilder,
    private titlecasePipe: TitleCasePipe) { }

  ngOnInit(): void {
    this.semestreForm = this.formBuilder.group({
      name: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ]
    });

    if (this.data.isEditingSemestre) {
      this.semestreForm.setValue({ name: this.data.editingSemestre.name });
    }
  }

  createOrUpdate(formDirective: FormGroupDirective): void {
    if (this.data.isEditingSemestre) {
      this.update();
    } else {
      this.create(formDirective);
    }
  }

  create(formDirective: FormGroupDirective): void {
    const semestre: Semestre = this.semestreForm.getRawValue() as Semestre;
    semestre.name = this.titlecasePipe.transform(semestre.name);
    this.semestreService.create(semestre).subscribe({
      next: (semestre) => {
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
    this.semestreForm.reset();
    this.dialogRef.close();
  }

  update(): void {
    this.data.editingSemestre.name = this.titlecasePipe.transform(this.semestreForm.get('name')?.value);
    this.semestreService.update(this.data.editingSemestre).subscribe({
      next: (semestre) => {
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
    this.data.isEditingSemestre = false;
    this.semestreForm.reset();
    this.dialogRef.close();
  }

  edit(semestre: Semestre): void {
    this.data.isEditingSemestre = true;
    this.data.editingSemestre = semestre;
    this.semestreForm.setValue({ name: this.data.editingSemestre.name });
  }

  cancelEdit(): void {
    this.dialogRef.close();
  }

  getErrorMessage(field: string): string {
    if (this.semestreForm.get(field)?.hasError('required')) {
      return `Requerido`;
    }

    if (this.semestreForm.get(field)?.hasError('minlength')) {
      return `Deve ter no mínimo ${this.semestreForm.get(field)?.errors?.['minlength'].requiredLength} caracteres`;
    }

    return this.semestreForm.get(field)?.invalid ? `Campo inválido ${field}` : '';
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
