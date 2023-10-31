import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {markAllAsDirty} from "../../../../utils/form.utils";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-wish',
  templateUrl: './add-wish.component.html',
  styleUrls: ['./add-wish.component.scss']
})
export class AddWishComponent implements OnInit {

  wishForm!: FormGroup;

  constructor(
    private _fb: FormBuilder,
    private _dialogRef: MatDialogRef<AddWishComponent>
  ) { }

  ngOnInit(): void {
    this.wishForm = this._fb.group({
      link: this._fb.control<string>("", [Validators.required]),
      name: this._fb.control<string>(""),
      price: this._fb.control<number | null>(null, [Validators.min(0), Validators.pattern(/^[0-9]+$/)]),
      comment: this._fb.control<string>("")
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
