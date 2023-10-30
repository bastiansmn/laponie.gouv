import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User, UserCreation} from "../model/user.model";
import {Wish} from "../model/wish.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) { }

  getUserByEmail(email: string) {
    email = encodeURI(email);
    return this.http.get<User>(`/api/user?email=${email}`)
  }

  searchByEmail(email: string) {
    email = encodeURI(email);
    return this.http.get<User[]>(`/api/user/search?email=${email}`)
  }

  getFamilies(id: number) {
    return this.http.get<any>(`/api/user/families?id=${id}`)
  }

  createUser(user: UserCreation) {
    return this.http.post<User>('/api/user', user)
  }

  getWishes(userID: number) {
    return this.http.get<Wish[]>(`/api/user/wishes?id=${userID}`)
  }

}
