import {Component, OnInit} from '@angular/core';
import {LocalStorageService} from "./services/local-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(
    private _localStorageService: LocalStorageService,
    private _router: Router
  ) { }

  ngOnInit(): void {
    const loggedEmail = this._localStorageService.getData('email')
    const stringifiedUser = this._localStorageService.getData('user');

    if (!loggedEmail || !stringifiedUser) {
      this._router.navigate(['/auth'])
      return;
    }
  }

}
