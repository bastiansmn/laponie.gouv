import {Component} from '@angular/core';
import {LoaderService} from "./services/loader.service";

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
