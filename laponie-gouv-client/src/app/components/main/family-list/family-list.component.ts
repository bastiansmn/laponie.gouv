import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../services/user.service";
import {User} from "../../../model/user.model";
import {take} from "rxjs";
import {Router} from "@angular/router";
import {Family} from "../../../model/family.model";

@Component({
  selector: 'app-family-list',
  templateUrl: './family-list.component.html',
  styleUrls: ['./family-list.component.css']
})
export class FamilyListComponent implements OnInit {

  families: Family[] = [];

  constructor(
    private _userService: UserService,
    private _router: Router
  ) { }

  ngOnInit(): void {
    const stringifiedUser = localStorage.getItem('user');
    if (!stringifiedUser) {
      this._router.navigate(['/auth']);
      return;
    }

    const loggedUser = JSON.parse(stringifiedUser) as User;
    this._userService.getFamilies(loggedUser.id)
      .pipe(take(1))
      .subscribe(families => {
        this.families = families;
      });
  }

}
