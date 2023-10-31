import {Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(
    private _snackBar: MatSnackBar
  ) { }

  show(message: string, duration = -1) {
    const snackBarRef = this._snackBar.open(message, 'Fermer');

    if (duration > 0)
      snackBarRef._dismissAfter(duration);
  }

}
