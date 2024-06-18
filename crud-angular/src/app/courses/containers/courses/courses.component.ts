import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, Observable, of } from 'rxjs';

import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { Course } from '../../model/course';
import { CoursesService } from '../../services/courses.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent implements OnInit {

  courses$: Observable<Course[]> | null = null;
  displayedColumns = ['name', 'category', 'actions'];

  // coursesService: CoursesService;

  constructor(
    private coursesService: CoursesService,
    public dialog: MatDialog,
    private router: Router,
    private _snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {
   this.refresh();
  }

  ngOnInit(): void {
  }

  onAdd(){
    this.router.navigate(['new'], {relativeTo: this.route});
  }
  onEdit(course: Course){
    this.router.navigate(['edit', course._id], {relativeTo: this.route});
  }


  refresh(){
    this.courses$ = this.coursesService.list()
      .pipe(
        catchError(error => {
          this.onError("Erro ao carregar cursos");
          return of([])
        })
      );
  }

  onDelete(course: Course){
    this.coursesService.remove(course._id).subscribe(
      () => {
        this.refresh();
        this._snackBar.open("Curso removido com sucesso!", " X ",
          {duration: 3000,
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
      },
      error => this.onError("Erro ao tentar remover curso")
    );
  }


  onError(errorMsg: string) {
    this.dialog.open(ErrorDialogComponent, {
      data: errorMsg
    });

  }
}
