import {AbstractControl, FormArray, FormGroup} from "@angular/forms";

export function markAllAsDirty(ac: AbstractControl) {
  if (ac instanceof FormGroup) {
    Object.keys(ac.controls).forEach(controlName => {
      markAllAsDirty(ac.controls[controlName])
    })
    return;
  }

  if (ac instanceof FormArray) {
    ac.controls.forEach(control => {
      markAllAsDirty(control)
    })
    return;
  }

  ac.markAsDirty()
}
