import { TitleCasePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormBuilder, FormGroup, FormGroupDirective, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Curso } from './curso';
import { CursoService } from './curso.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
//import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@Component({
  selector: 'app-curso',
  imports: [
    HttpClientModule,
    //BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
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

  cursos: Curso[] = [];
  displayedColumns: string[] = ['id', 'name', 'action'];
  cursoForm!: FormGroup;
  isEditingCurso: boolean = false;
  editingCurso!: Curso;

  constructor(
    private cursoService: CursoService,
    private formBuilder: FormBuilder,
    private titlecasePipe: TitleCasePipe) { }

  ngOnInit(): void {
    this.list();
    this.cursoForm = this.formBuilder.group({
      name: [
        '',
        [Validators.required, Validators.minLength(3)],
        ''
      ]
    });
  }

  list(): void {
    this.cursoService.list().subscribe(cursos => this.cursos = cursos);
  }

  create(formDirective: FormGroupDirective): void {
    const curso: Curso = this.cursoForm.getRawValue() as Curso;
    curso.name = this.titlecasePipe.transform(curso.name);
    this.cursoService.create(curso).subscribe(curso => this.list());
    formDirective.resetForm();
    this.cursoForm.reset();    
  }

  update(): void {
    this.editingCurso.name = this.titlecasePipe.transform(this.cursoForm.get('name')?.value);
    this.cursoService.update(this.editingCurso).subscribe(curso => this.list());
    this.isEditingCurso = false;
    this.cursoForm.reset();
  }

  edit(curso: Curso): void {
    this.isEditingCurso = true;
    this.editingCurso = curso;
    this.cursoForm.setValue({ name: this.editingCurso.name });
  }

  cancelEdit(): void {
    this.isEditingCurso = false;
    this.cursoForm.reset();
  }

  delete(curso: Curso): void {
    this.cursoService.delete(curso).subscribe(curso => this.list());
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


}
