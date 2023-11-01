import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Wish} from "../../../../model/wish.model";
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {markAllAsDirty} from "../../../../utils/form.utils";
import {CustomErrorStateMatcher} from "../../../../utils/custom-error-state.matcher";
import {User} from "../../../../model/user.model";

@Component({
  selector: 'app-mark-as-gifted',
  templateUrl: './mark-as-gifted.component.html',
  styleUrls: ['./mark-as-gifted.component.css']
})
export class MarkAsGiftedComponent implements OnInit {

  gifter!: FormControl;

  matcher = new CustomErrorStateMatcher();

  get wish() {
    return this._wish;
  }

  get connectedUser() {
    const user = localStorage.getItem('user');
    if (user === null) return null;
    return JSON.parse(user) as User;
  }

  constructor(
    public dialogRef: MatDialogRef<MarkAsGiftedComponent>,
    @Inject(MAT_DIALOG_DATA) private _wish: Wish,
    private _fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.gifter = this._fb.control<string>(this.connectedUser?.name ?? '', [Validators.required]);
  }

  handleSubmit() {
    markAllAsDirty(this.gifter);

    if (this.gifter.invalid) {
      return;
    }

    this.dialogRef.close(this.gifter.value);
  }

  handleCancel() {
    this.dialogRef.close();
  }
}
