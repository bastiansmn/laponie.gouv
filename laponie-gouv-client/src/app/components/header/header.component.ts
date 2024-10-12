import {Component} from '@angular/core';
import {AppService} from "../../services/app.service";
import {LocalStorageService} from "../../services/local-storage.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  get sidenavToggled() {
    return this._appService.sidenavToggled;
  }

  constructor(
    private _appService: AppService,
    private _localStorageService: LocalStorageService
  ) { }

  toggleSidenav() {
    this._appService.toggleSidenav();
  }

  disconnect() {
    this._localStorageService.clearData();
    window.location.reload();
  }
}
