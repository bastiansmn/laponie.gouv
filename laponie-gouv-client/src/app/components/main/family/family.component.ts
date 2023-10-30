import {Component, OnInit} from '@angular/core';
import {FamilyService} from "../../../services/family.service";
import {Family} from "../../../model/family.model";
import {ActivatedRoute} from "@angular/router";
import {take} from "rxjs";
import {User} from "../../../model/user.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {markAllAsDirty} from "../../../utils/form.utils";
import {WishService} from "../../../services/wish.service";
import {CustomErrorStateMatcher} from "../../../utils/custom-error-state.matcher";
import {Wish} from "../../../model/wish.model";
import {MatDialog} from "@angular/material/dialog";
import {MarkAsGiftedComponent} from "./mark-as-gifted/mark-as-gifted.component";
import {AddUserComponent} from "./add-user/add-user.component";

@Component({
  selector: 'app-family',
  templateUrl: './family.component.html',
  styleUrls: ['./family.component.css']
})
export class FamilyComponent implements OnInit {

  family!: Family;
  currentUser!: User;

  addWishForm!: FormGroup;

  matcher = new CustomErrorStateMatcher();

  get connectedUser() {
    return JSON.parse(localStorage.getItem('user')!) as User;
  }

  constructor(
    private _familyService: FamilyService,
    private _wishService: WishService,
    private _activatedRoute: ActivatedRoute,
    private _fb: FormBuilder,
    private _dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.addWishForm = this._fb.group({
      link: this._fb.control<string>('', [Validators.required])
    });

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
  }

  handleSubmitWish() {
    markAllAsDirty(this.addWishForm);

    if (this.addWishForm.invalid) {
      return;
    }

    this._wishService.createWish({ email: this.connectedUser.email, ...this.addWishForm.value })
      .pipe(take(1))
      .subscribe(wish => {
        console.log(wish);
        this.currentUser.wishes.push(wish);
      })
  }

  handleSelect(wish: Wish) {
    if (wish.gifted) return;

    const dialogRef = this._dialog.open(MarkAsGiftedComponent, {
      data: wish,
    });

    dialogRef.afterClosed()
      .pipe(take((1)))
      .subscribe(gifter => {
        if (!gifter) return;

        this._wishService.markAsGifted(wish.id, gifter)
          .pipe(take(1))
          .subscribe(wish => {
            this.currentUser.wishes = this.currentUser.wishes.map(w => w.id === wish.id ? wish : w);
          })
      });
  }

  handleDelete(wish: Wish) {
    this._wishService.deleteWish(wish.id, this.currentUser?.id)
      .pipe(take(1))
      .subscribe(() => {
        this.currentUser.wishes = this.currentUser.wishes.filter(w => w.id !== wish.id);
      });
  }

  openAddUserDialog() {
    const dialogRef = this._dialog.open(AddUserComponent);

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe(user => {
        if (!user) return;

        this._familyService.addUser(this.family.id, user)
          .pipe(take(1))
          .subscribe(family => {
            this.family = family;
          })
      })
  }
}
