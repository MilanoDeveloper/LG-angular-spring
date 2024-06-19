import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, NonNullableFormBuilder, UntypedFormArray, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

import { CoursesService } from '../../services/courses.service';
import { ActivatedRoute } from '@angular/router';
import { Course } from '../../model/course';
import { Lesson } from '../../model/lesson';

@Component({
  selector: 'app-course-form',
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent implements OnInit {

  form!: FormGroup;

  constructor(private formBuilder: NonNullableFormBuilder,
    private service: CoursesService,
    private _snackBar: MatSnackBar,
    private location: Location,
    private route: ActivatedRoute) {
  }

  onSubmit() {
    this.service.save(this.form.value).subscribe(data => this.onSucess(), error => {
      this.onError()
    });
  }

  getLessonsFormArray(){
    return (<UntypedFormArray>this.form.get('lessons')).controls;
  }

  onCancel() {
    this.location.back();
  }

  private onSucess() {
    this._snackBar.open("Curso salvo com sucesso!", "", { duration: 3000 });
    this.onCancel();
  }

  private onError() {
    this._snackBar.open("Erro ao salvar curso", "", { duration: 3000 });
  }

  errorMessage(fieldName: string) {
    const field = this.form.get(fieldName);

    if (field?.hasError('required')) {
      return "Campo Obrigatorio"
    }

    if (field?.hasError('minlength')) {
      const requiredLength = field.errors ? field.errors['minlength']['requiredLength'] : 5;
      return "Tamanho minimo precisa ser de ${requiredLength} caracteres";
    }
    if (field?.hasError('maxlength')) {
      const requiredLength = field.errors ? field.errors['maxlength']['requiredLength'] : 150;
      return 'Tamanho maximo excedido de ${requiredLength} caracteres';
    }

    return 'Campo Invalido';
  }


  ngOnInit(): void {
    const course: Course = this.route.snapshot.data['course'];

    this.form = this.formBuilder.group({
        _id: [course._id],
        name: [course.name, [Validators.required, Validators.minLength(5), Validators.maxLength(150)]],
        category: [course.category, [Validators.required]],
        lessons: this.formBuilder.array(this.retrieveLessons(course))
    });
  }

  private retrieveLessons(course: Course) {
    const lessons = [];
    if (course?.lessons) {
      course.lessons.forEach(lesson => lessons.push(this.createLesson(lesson)));
    } else {
      lessons.push(this.createLesson())
    }
    return lessons;
  }

  private createLesson(lesson: Lesson = { id: '', name: '', youtubeUrl: '' }) {
    return this.formBuilder.group({
      id: [lesson.id],
      name: [lesson.name],
      youtubeUrl: [lesson.youtubeUrl]
    })
  }


}

