import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {markAllAsDirty} from "../../../../utils/form.utils";

@Component({
  selector: 'app-add-kid-dialog',
  templateUrl: './add-kid-dialog.component.html',
  styleUrls: ['./add-kid-dialog.component.scss']
})
export class AddKidDialogComponent implements OnInit {

  kidNameControl!: FormControl;

  constructor(
    private _dialogRef: MatDialogRef<AddKidDialogComponent>,
    private _fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.kidNameControl = this._fb.control<string>('', [Validators.required]);
  }

  handleCancel() {
    this._dialogRef.close();
  }

  handleSave() {
    markAllAsDirty(this.kidNameControl);
    if (this.kidNameControl.invalid) return;
    this._dialogRef.close(this.kidNameControl.value);
  }
}
