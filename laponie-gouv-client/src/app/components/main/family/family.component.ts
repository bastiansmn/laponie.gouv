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
    private _fb: FormBuilder
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
}
