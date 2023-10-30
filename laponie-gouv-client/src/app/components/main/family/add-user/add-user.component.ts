import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl} from "@angular/forms";
import {UserService} from "../../../../services/user.service";
import {debounceTime, Subject, take, takeUntil} from "rxjs";
import {User} from "../../../../model/user.model";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit, OnDestroy {

  private componentDestroyed$ = new Subject<boolean>();

  email!: FormControl;

  suggestedUsers: User[] = [];

  constructor(
    private dialogRef: MatDialogRef<AddUserComponent>,
    private _fb: FormBuilder,
    private _userService: UserService
  ) { }

  ngOnInit(): void {
    this.email = this._fb.control<string>('');

    this.email.valueChanges
      .pipe(
        takeUntil(this.componentDestroyed$),
        debounceTime(500)
      )
      .subscribe(value => {
        this._userService.searchByEmail(value)
          .pipe(take(1))
          .subscribe(users => {
            this.suggestedUsers = users;
          })
      })
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  handleOptionSelected($event: MatAutocompleteSelectedEvent ) {
    this.dialogRef.close($event.option.value);
  }
}
