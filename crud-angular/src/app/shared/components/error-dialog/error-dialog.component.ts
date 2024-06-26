import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose } from '@angular/material/dialog';

@Component({
    selector: 'app-error-dialog',
    templateUrl: './error-dialog.component.html',
    styleUrl: './error-dialog.component.scss',
    standalone: true,
    imports: [MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose]
})
export class ErrorDialogComponent implements OnInit{

  constructor(@Inject(MAT_DIALOG_DATA) public data: string) {}

  ngOnInit(): void {

  }
}
