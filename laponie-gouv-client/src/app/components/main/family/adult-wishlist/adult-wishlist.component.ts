import {Component, Input} from '@angular/core';
import {User} from "../../../../model/user.model";
import {Wish} from "../../../../model/wish.model";
import {MarkAsGiftedComponent} from "../mark-as-gifted/mark-as-gifted.component";
import {take} from "rxjs";
import {ConfirmModalComponent} from "../../../confirm-modal/confirm-modal.component";
import {AddWishComponent} from "../add-wish/add-wish.component";
import {MatDialog} from "@angular/material/dialog";
import {LoaderService} from "../../../../services/loader.service";
import {WishService} from "../../../../services/wish.service";

@Component({
  selector: 'app-adult-wishlist',
  templateUrl: './adult-wishlist.component.html',
  styleUrls: ['./adult-wishlist.component.css']
})
export class AdultWishlistComponent {

  @Input() currentUser!: User;

  get connectedUser() {
    const user = localStorage.getItem('user');
    if (user === null) return null;
    return JSON.parse(user) as User;
  }

  constructor(
    private _dialog: MatDialog,
    private _loaderService: LoaderService,
    private _wishService: WishService,
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
            if (this.currentUser) {
              this.currentUser.wishes = this.currentUser.wishes.map(w => w.id === wish.id ? wish : w);
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
        this._wishService.deleteWish(wish.id, this.currentUser?.id)
          .pipe(take(1))
          .subscribe(() => {
            if (this.currentUser) {
              this.currentUser.wishes = this.currentUser.wishes.filter(w => w.id !== wish.id);
            }
          });
      });

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
            if (this.currentUser) {
              this.currentUser.wishes.push(w);
            }
          })
      })
  }

}
