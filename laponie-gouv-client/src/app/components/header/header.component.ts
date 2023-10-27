import { Component } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  private sidenavToggled$ = new BehaviorSubject<boolean>(false);
  get sidenavToggled() {
    return this.sidenavToggled$.asObservable()
  }

  toggleSidenav() {
    this.sidenavToggled$.next(!this.sidenavToggled$.value);
  }
}
