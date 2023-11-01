import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {catchError, Observable, take} from 'rxjs';
import {LocalStorageService} from "../services/local-storage.service";
import {LoaderService} from "../services/loader.service";
import {UserService} from "../services/user.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(
    private _localStorageService: LocalStorageService,
    private _router: Router,
    private _loaderService: LoaderService,
    private _userService: UserService
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (route.queryParams['email']) {
      const email = decodeURI(route.queryParams['email']);
      // If mail doesn't match email regex
      if (email.match(/^[a-z0-9._-]+@[a-z0-9._-]+\.[a-z]{2,4}$/i)) {
        this._loaderService.show();
        this._userService.getUserByEmail(email)
          .pipe(
            take(1),
            catchError((err) => {
              console.error(err);
              return this._userService.createUser({ email })
                .pipe(take(1))
            })
          )
          .subscribe(user => {
            // On sauvegarde l'utilisateur dans le local storage
            this._localStorageService.saveData('email', user.email)
            this._localStorageService.saveData('user', JSON.stringify(user));

            if (route.queryParams['redirect']) {
              this._router.navigate([decodeURI(route.queryParams['redirect'])]);
            }
          })
      }
    }

    const loggedEmail = this._localStorageService.getData('email')
    const stringifiedUser = this._localStorageService.getData('user');

    if (!loggedEmail || !stringifiedUser) {
      this._router.navigate(['/auth']);
    }

    return true;
  }

}
