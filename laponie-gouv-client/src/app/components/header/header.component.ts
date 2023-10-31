import {Component} from '@angular/core';
import {AppService} from "../../services/app.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(
    private _appService: AppService
  ) { }

  toggleSidenav() {
    this._appService.toggleSidenav();
  }
}
