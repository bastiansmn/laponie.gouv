import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomErrorStateMatcher} from "../../utils/custom-error-state.matcher";
import {markAllAsDirty} from "../../utils/form.utils";
import {UserService} from "../../services/user.service";
import {catchError, take} from "rxjs";
import {LocalStorageService} from "../../services/local-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {

  constructor(
    private _fb: FormBuilder,
    private _userService: UserService,
    private _localStorageService: LocalStorageService,
    private _router: Router
  ) { }

  authForm!: FormGroup;
  matcher = new CustomErrorStateMatcher();

  ngOnInit(): void {
    this.authForm = this._fb.group({
      email: this._fb.control('', [Validators.required, Validators.email]),
      name: this._fb.control('', [Validators.required]),
    })
  }

  handleConnection() {
    markAllAsDirty(this.authForm);

    if (this.authForm.invalid)
      return;

    // Si l'utilisateur existe, on le charge, sinon on le crée
    this._userService.getUserByEmail(this.authForm.value.email)
      .pipe(
        take(1),
        catchError((err) => {
          console.error(err);
          return this._userService.createUser(this.authForm.value)
            .pipe(take(1))
        })
      )
      .subscribe(user => {
        // On sauvegarde l'utilisateur dans le local storage
        this._localStorageService.saveData('email', user.email)
        this._localStorageService.saveData('user', JSON.stringify(user));

        this._router.navigate(['/home']);
      })
  }
}
