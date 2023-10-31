import {Component, OnInit} from '@angular/core';
import {LocalStorageService} from "./services/local-storage.service";
import {Router} from "@angular/router";
import {LoaderService} from "./services/loader.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  get isLoading() {
    return this._loaderService.isLoading;
  }

  constructor(
    private _localStorageService: LocalStorageService,
    private _router: Router,
    private _loaderService: LoaderService
  ) { }

  ngOnInit(): void {
    const loggedEmail = this._localStorageService.getData('email')
    const stringifiedUser = this._localStorageService.getData('user');

    if (!loggedEmail || !stringifiedUser) {
      this._router.navigate(['/auth']);
    }
  }

}
