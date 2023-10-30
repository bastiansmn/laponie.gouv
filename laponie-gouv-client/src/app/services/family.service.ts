import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Family, FamilyCreation} from "../model/family.model";
import {User} from "../model/user.model";

@Injectable({
  providedIn: 'root'
})
export class FamilyService {

  constructor(
    private http: HttpClient
  ) { }

  getFamily(id: number) {
    return this.http.get<Family>(`api/family?id=${id}`)
  }

  createFamily(family: FamilyCreation) {
    return this.http.post<Family>('api/family', family)
  }

  addUser(id: number, user: User) {
    return this.http.put<Family>(`api/family?id=${id}`, user)
  }

  removeUser(id: number, user: User) {
    return this.http.delete<void>(`api/family?id=${id}`, {body: user})
  }
}
