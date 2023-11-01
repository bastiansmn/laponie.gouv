import {Component, OnInit} from '@angular/core';
import {FamilyService} from "../../../services/family.service";
import {Family} from "../../../model/family.model";
import {ActivatedRoute} from "@angular/router";
import {Observable, take} from "rxjs";
import {User} from "../../../model/user.model";
import {WishService} from "../../../services/wish.service";
import {Wish} from "../../../model/wish.model";
import {MatDialog} from "@angular/material/dialog";
import {MarkAsGiftedComponent} from "./mark-as-gifted/mark-as-gifted.component";
import {AddUserComponent} from "./add-user/add-user.component";
import {AddWishComponent} from "./add-wish/add-wish.component";
import {AppService} from "../../../services/app.service";
import {LoaderService} from "../../../services/loader.service";
import {ConfirmModalComponent} from "../../confirm-modal/confirm-modal.component";

@Component({
  selector: 'app-family',
  templateUrl: './family.component.html',
  styleUrls: ['./family.component.css']
})
export class FamilyComponent implements OnInit {

  family!: Family;
  currentUser!: User;

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
    private _wishService: WishService,
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
    this._appService.toggleSidenav();
  }

  handleSelect(wish: Wish) {
    if (wish.gifted) return;

    const dialogRef = this._dialog.open(MarkAsGiftedComponent, {
      data: wish,
      width: '600px',
    });

    dialogRef.afterClosed()
      .pipe(take((1)))
      .subscribe(gifter => {
        if (!gifter || !this.connectedUser?.email) return;

        this._loaderService.show();
        this._wishService.markAsGifted(wish.id, gifter, this.connectedUser?.email)
          .pipe(take(1))
          .subscribe(wish => {
            this.currentUser.wishes = this.currentUser.wishes.map(w => w.id === wish.id ? wish : w);
          })
      });
  }

  handleDelete(wish: Wish) {
    const dialogRef = this._dialog.open(ConfirmModalComponent, {
      data: {
        title: 'Supprimer le souhait ?',
      },
      width: '300px',
    })

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(result => {
        if (!result) return;

        this._loaderService.show();
        this._wishService.deleteWish(wish.id, this.currentUser?.id)
          .pipe(take(1))
          .subscribe(() => {
            this.currentUser.wishes = this.currentUser.wishes.filter(w => w.id !== wish.id);
          });
      });

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

  openAddWishDialog() {
    const dialogRef = this._dialog.open(AddWishComponent, {
      width: '400px',
    });

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(wish => {
        if (!wish) return;

        this._loaderService.show();
        this._wishService.createWish({ email: this.connectedUser?.email, ...wish })
          .pipe(take(1))
          .subscribe(w => {
            this.currentUser.wishes.push(w);
          })
      })
  }

  handleOpenWish(wish: Wish) {
    console.log( wish );
  }
}
