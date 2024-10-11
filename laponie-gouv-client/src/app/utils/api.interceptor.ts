import {Injectable} from "@angular/core";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import {catchError, finalize, Observable, throwError} from "rxjs";
import ApiError from "../model/api-error.model";
import {AlertService} from "../services/alert.service";
import {LoaderService} from "../services/loader.service";


@Injectable()
export class ApiInterceptor implements HttpInterceptor {

  constructor(
    private _alertService: AlertService,
    private _loaderService: LoaderService
  ) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    return next.handle(request).pipe(

      finalize(() => {
        this._loaderService.hide();
      }),
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }

  private handleError(err: HttpErrorResponse) {
    const backendError = err.error as ApiError;

    console.error(backendError.message);

    this._loaderService.hide();
    if (err.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('Une erreur a eu lieu:', err.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.

      this._alertService.show(backendError.message, 5000);

      console.error(`Backend a retourné le code ${err.status}, le body était: `, backendError);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Une erreur a eu lieu,  merci de bien vouloir re essayer ultérieurement'));
  }

}
