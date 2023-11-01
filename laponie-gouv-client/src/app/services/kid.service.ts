import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {KidWishCreation, Wish} from "../model/wish.model";
import {Kid} from "../model/kid.model";

@Injectable({
  providedIn: 'root'
})
export class KidService {

  constructor(
    private http: HttpClient
  ) { }

  createKid(name: string) {
    return this.http.post<Kid>(`/api/kid?name=${encodeURI(name)}`, null);
  }

  createKidWish(wish: KidWishCreation) {
    return this.http.post<Wish>(`/api/kid/wish`, wish);
  }
}
