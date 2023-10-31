import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  private sidenavToggled$ = new BehaviorSubject<boolean>(true);
  get sidenavToggled() {
    return this.sidenavToggled$.asObservable();
  }

  toggleSidenav() {
    this.sidenavToggled$.next(!this.sidenavToggled$.value);
  }
}
