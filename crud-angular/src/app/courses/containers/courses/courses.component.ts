import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, catchError, of, tap } from 'rxjs';

import { AsyncPipe, NgIf } from '@angular/common';
import { MatCard, MatCardContent } from '@angular/material/card';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatToolbar } from '@angular/material/toolbar';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { CoursesListComponent } from '../../components/courses-list/courses-list.component';
import { Course } from '../../model/course';
import { CoursesService } from '../../services/courses.service';
import { CoursePage } from '../../model/course-page';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrl: './courses.component.scss',
    standalone: true,
    imports: [MatCard, MatCardContent, MatToolbar, NgIf, CoursesListComponent, MatProgressSpinner, AsyncPipe, MatPaginatorModule]
})
export class CoursesComponent implements OnInit {

  courses$: Observable<CoursePage> | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  pageIndex = 0;
  pageSize = 10;

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

  ngOnInit(): void {}

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }
  onEdit(course: Course) {
    this.router.navigate(['edit', course._id], { relativeTo: this.route });
  }


  refresh(pageEvent: PageEvent = {length: 0, pageIndex: 0, pageSize: 10}) {
    this.courses$ = this.coursesService.list(pageEvent.pageIndex, pageEvent.pageSize)
      .pipe(


        catchError(error => {
          this.onError("Erro ao carregar cursos");
          return of({courses: [], totalElements: 0, totalPages: 0 })
        })
      );
  }

  onDelete(course: Course) {

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Tem certeza que deseja remover o curso?',
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.coursesService.remove(course._id).subscribe(
          () => {
            this.refresh();
            this._snackBar.open("Curso removido com sucesso!", " X ",
              {
                duration: 3000,
                verticalPosition: 'top',
                horizontalPosition: 'center'
              });
          },
          error => this.onError("Erro ao tentar remover curso")
        );
      }
    });

  }


  onError(errorMsg: string) {
    this.dialog.open(ErrorDialogComponent, {
      data: errorMsg
    });

  }
}
