import {Component, OnInit} from '@angular/core';
import {FamilyService} from "../../../services/family.service";
import {Family} from "../../../model/family.model";
import {ActivatedRoute} from "@angular/router";
import {Observable, take} from "rxjs";
import {User} from "../../../model/user.model";
import {MatDialog} from "@angular/material/dialog";
import {AddUserComponent} from "./add-user/add-user.component";
import {AppService} from "../../../services/app.service";
import {LoaderService} from "../../../services/loader.service";
import {AddKidDialogComponent} from "./add-kid-dialog/add-kid-dialog.component";
import {Kid} from "../../../model/kid.model";

@Component({
  selector: 'app-family',
  templateUrl: './family.component.html',
  styleUrls: ['./family.component.css']
})
export class FamilyComponent implements OnInit {

  family!: Family;
  currentUser!: User | null;
  currentKid!: Kid | null;
  get getUserSorted() {
    return this.family.users.sort((a, b) => a.id - b.id);
  }

  get connectedUser() {
    const user = localStorage.getItem('user');
    if (user === null) return null;
    return JSON.parse(user) as User;
  }

  get sidenavToggled(): Observable<boolean> {
    return this._appService.sidenavToggled;
  }

  constructor(
    private _familyService: FamilyService,
    private _loaderService: LoaderService,
    private _appService: AppService,
    private _activatedRoute: ActivatedRoute,
    private _dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this._appService.forceSidenav(true);

    this._loaderService.show();

    this._activatedRoute.params
      .pipe(take(1))
      .subscribe(params => {
        this._familyService.getFamily(params['id'])
          .pipe(take(1))
          .subscribe(family => {
            this.family = family;
          })
      })
  }

  selectUser(user: User) {
    this.currentUser = user;
    this.currentKid = null;
    this._appService.forceSidenav(false);
  }

  selectKid(kid: Kid) {
    this.currentKid = kid;
    this.currentUser = null;
    this._appService.forceSidenav(false);
  }

  openAddUserDialog() {
    const dialogRef = this._dialog.open(AddUserComponent, {
      width: '400px',
    });

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(user => {
        if (!user) return;

        this._loaderService.show();
        this._familyService.addUser(this.family.id, user)
          .pipe(take(1))
          .subscribe(family => {
            this.family = family;
          })
      })
  }

  openAddKidDialog() {
    const dialogRef = this._dialog.open(AddKidDialogComponent, {
      width: '400px',
    });

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(kid => {
        if (!kid) return;

        this._loaderService.show();
        this._familyService.addKid(this.family.id, kid)
          .pipe(take(1))
          .subscribe(f => {
            this.family = f;
          })
      })
  }

}
