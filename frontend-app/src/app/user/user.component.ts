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
import { User } from './user';
import { UserService } from './user.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface IUserDialog {
  editingUser: User;
  isEditingUser: boolean;
  list: () => {};
}

@Component({
  selector: 'app-user',
  imports: [
    MatTableModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
  providers: [TitleCasePipe]
})
export class UserComponent implements OnInit {
  private _snackBar = inject(MatSnackBar);
  
  users: User[] = [];
  displayedColumns: string[] = ['username', 'name', 'email', 'action'];
  isCreatingUser: boolean = false;
  isEditingUser: boolean = false;
  editingUser!: User;
  errorMessage: string | null = null;
  readonly dialog = inject(MatDialog);
  
  constructor(private userService: UserService, public confirmDialog: MatDialog) { }

  ngOnInit(): void {
    this.list();
  }

  openConfirmDialog(user: User): void {
    const confirmDialogRef = this.confirmDialog.open(ConfirmDialogComponent, {
      data: { title: `o Usuário ${user.firstName} ${user.lastName}`, delete: () => this.delete(user) }
    });

    confirmDialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openUserDialog(user?: User): void {
    const dialogRef = this.dialog.open(UserDialog, {
      data: {
        editingUser: user,
        isEditingUser: !!user,
        list: () => this.list()
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result !== undefined) {
        this.editingUser.firstName = result;
      }
    });
  }

  list(): void {
    this.userService.list().subscribe((users) => this.users = users);
  }

  delete(user: User): void {
    this.userService.delete(user).subscribe({
      next: (user) => {
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
  selector: 'user-dialog',
  templateUrl: 'user.dialog.component.html',
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
export class UserDialog {
  private _snackBar = inject(MatSnackBar);
  readonly dialogRef = inject(MatDialogRef<UserDialog>);
  readonly data = inject<IUserDialog>(MAT_DIALOG_DATA);

  userForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private titlecasePipe: TitleCasePipe) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
      username: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ],
      firstName: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ],
      lastName: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ],
      email: [
        '',
        [Validators.required, Validators.email],
        ''
      ]
    });

    if (this.data.isEditingUser) {
      this.userForm.setValue({
        username: this.data.editingUser.username,
        firstName: this.data.editingUser.firstName,
        lastName: this.data.editingUser.lastName,
        email: this.data.editingUser.email
      });
    }
  }

  createOrUpdate(formDirective: FormGroupDirective): void {
    if (this.data.isEditingUser) {
      this.update();
    } else {
      this.create(formDirective);
    }
  }

  create(formDirective: FormGroupDirective): void {
    const user: User = this.userForm.getRawValue() as User;
    user.firstName = this.titlecasePipe.transform(user.firstName);
    user.lastName = this.titlecasePipe.transform(user.lastName);
    this.userService.create(user).subscribe({
      next: (user) => {
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
    this.userForm.reset();
    this.dialogRef.close();
  }

  update(): void {
    this.data.editingUser.firstName = this.titlecasePipe.transform(this.userForm.get('firstName')?.value);
    this.data.editingUser.lastName = this.titlecasePipe.transform(this.userForm.get('lastName')?.value);
    this.userService.update(this.data.editingUser).subscribe({
      next: (user) => {
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
    this.data.isEditingUser = false;
    this.userForm.reset();
    this.dialogRef.close();
  }

  edit(user: User): void {
    this.data.isEditingUser = true;
    this.data.editingUser = user;
    this.userForm.setValue({
      username: this.data.editingUser.username,
      firstName: this.data.editingUser.firstName,
      lastName: this.data.editingUser.lastName,
      email: this.data.editingUser.email
    });
  }

  cancelEdit(): void {
    this.dialogRef.close();
  }

  getErrorMessage(field: string): string {
    if (this.userForm.get(field)?.hasError('required')) {
      return `Requerido`;
    }

    if (this.userForm.get(field)?.hasError('minlength')) {
      return `Deve ter no mínimo ${this.userForm.get(field)?.errors?.['minlength'].requiredLength} caracteres`;
    }

    return this.userForm.get(field)?.invalid ? `Campo inválido ${field}` : '';
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
