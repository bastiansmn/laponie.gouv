import {ErrorStateMatcher} from "@angular/material/core";
import {AbstractControl, FormGroupDirective, NgForm} from "@angular/forms";

export class CustomErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: AbstractControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return (control?.invalid && control?.dirty) ?? false;
  }

}
