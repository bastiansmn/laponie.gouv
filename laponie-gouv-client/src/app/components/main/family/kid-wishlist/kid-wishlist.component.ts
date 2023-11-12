import {Component, Input} from '@angular/core';
import {User} from "../../../../model/user.model";
import {MatDialog} from "@angular/material/dialog";
import {LoaderService} from "../../../../services/loader.service";
import {WishService} from "../../../../services/wish.service";
import {Wish} from "../../../../model/wish.model";
import {MarkAsGiftedComponent} from "../mark-as-gifted/mark-as-gifted.component";
import {take} from "rxjs";
import {ConfirmModalComponent} from "../../../confirm-modal/confirm-modal.component";
import {AddWishComponent} from "../add-wish/add-wish.component";
import {Kid} from "../../../../model/kid.model";
import {KidService} from "../../../../services/kid.service";
import {AlertService} from "../../../../services/alert.service";

@Component({
  selector: 'app-kid-wishlist',
  templateUrl: './kid-wishlist.component.html',
  styleUrls: ['./kid-wishlist.component.css']
})
export class KidWishlistComponent {

  @Input() currentKid!: Kid;

  get connectedUser() {
    const user = localStorage.getItem('user');
    if (user === null) return null;
    return JSON.parse(user) as User;
  }

  constructor(
    private _dialog: MatDialog,
    private _loaderService: LoaderService,
    private _wishService: WishService,
    private _kidService: KidService,
    private _alertService: AlertService
  ) { }

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
            if (this.currentKid) {
              this.currentKid.wishes = this.currentKid.wishes.map(w => w.id === wish.id ? wish : w);
            }
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
        this._wishService.deleteWish(wish.id, this.currentKid?.id)
          .pipe(take(1))
          .subscribe(() => {
            if (this.currentKid) {
              this.currentKid.wishes = this.currentKid.wishes.filter(w => w.id !== wish.id);
            }
          });
      });

  }

  handleEdit(wish: Wish) {
    const dialogRef = this._dialog.open(AddWishComponent, {
      width: '400px',
      data: wish
    });

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(w1 => {
        if (!w1) return;

        this._loaderService.show();
        this._wishService.editWish({ email: this.connectedUser?.email, ...w1 }, wish.id)
          .pipe(take(1))
          .subscribe(resWish => {
            if (this.currentKid) {
              this._alertService.show(
                'Souhait modifié',
                5000
              )
              this.currentKid.wishes = this.currentKid.wishes.map(w => w.id === resWish.id ? resWish : w);
            }
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
        this._kidService.createKidWish({ kidID: this.currentKid.id, ...wish })
          .pipe(take(1))
          .subscribe(w => {
            if (this.currentKid) {
              this.currentKid.wishes.push(w);
            }
          })
      })
  }

}
