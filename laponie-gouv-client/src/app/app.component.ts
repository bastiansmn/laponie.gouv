import {Component, OnInit} from '@angular/core';
import {LocalStorageService} from "./services/local-storage.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LoaderService} from "./services/loader.service";
import {catchError, take} from "rxjs";
import {UserService} from "./services/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  get isLoading() {
    return this._loaderService.isLoading;
  }

  constructor(
    private _loaderService: LoaderService
  ) { }

}
