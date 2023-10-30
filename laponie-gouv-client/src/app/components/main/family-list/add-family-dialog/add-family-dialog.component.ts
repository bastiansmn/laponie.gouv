import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomErrorStateMatcher} from "../../../../utils/custom-error-state.matcher";
import {MatDialogRef} from "@angular/material/dialog";
import {markAllAsDirty} from "../../../../utils/form.utils";

@Component({
  selector: 'app-add-family-dialog',
  templateUrl: './add-family-dialog.component.html',
  styleUrls: ['./add-family-dialog.component.scss']
})
export class AddFamilyDialogComponent implements OnInit {

  familyForm!: FormGroup;

  matcher = new CustomErrorStateMatcher();

  constructor(
    public dialogRef: MatDialogRef<AddFamilyDialogComponent>,
    private _fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.familyForm = this._fb.group({
      name: this._fb.control<string>("", [Validators.required])
    });
  }

  handleSubmit() {
    markAllAsDirty(this.familyForm);

    if (this.familyForm.invalid) {
      return;
    }

    this.dialogRef.close(this.familyForm.value);
  }

}
