import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../services/user.service";
import {User} from "../../../model/user.model";
import {take} from "rxjs";
import {Router} from "@angular/router";
import {Family} from "../../../model/family.model";
import {MatDialog} from "@angular/material/dialog";
import {AddFamilyDialogComponent} from "./add-family-dialog/add-family-dialog.component";
import {FamilyService} from "../../../services/family.service";

@Component({
  selector: 'app-family-list',
  templateUrl: './family-list.component.html',
  styleUrls: ['./family-list.component.css']
})
export class FamilyListComponent implements OnInit {

  families: Family[] = [];

  constructor(
    private _userService: UserService,
    private _familyService: FamilyService,
    private _router: Router,
    private _dialog: MatDialog
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

  navigateTo(link: string) {
    this._router.navigate([link]);
  }

  openAddFamilyDialog() {
    const dialogRef = this._dialog.open(AddFamilyDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(result => {
        if (!result)
          return;

        this._familyService.createFamily(result)
          .pipe(take(1))
          .subscribe(family => {
            this.families.push(family);
          });
      });
  }
}
