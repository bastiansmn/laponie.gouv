import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {markAllAsDirty} from "../../../../utils/form.utils";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Wish} from "../../../../model/wish.model";

@Component({
  selector: 'app-add-wish',
  templateUrl: './add-wish.component.html',
  styleUrls: ['./add-wish.component.scss']
})
export class AddWishComponent implements OnInit {

  wishForm!: FormGroup;

  constructor(
    private _fb: FormBuilder,
    private _dialogRef: MatDialogRef<AddWishComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Wish | null,
  ) { }

  ngOnInit(): void {
    this.wishForm = this._fb.group({
      link: this._fb.control<string>(this.data?.link ?? '', [Validators.required]),
      name: this._fb.control<string>(this.data?.name ?? ''),
      price: this._fb.control<number | null>(this.data?.price ?? null, [Validators.min(0)]),
      comment: this._fb.control<string>(this.data?.comment ?? '')
    })
  }

  handleSubmit() {
    markAllAsDirty(this.wishForm);

    if (this.wishForm.invalid) return;

    this._dialogRef.close(this.wishForm.value);
  }

  handleCancel() {
    this._dialogRef.close();
  }
}
