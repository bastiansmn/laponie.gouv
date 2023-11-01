import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Wish, WishCreation} from "../model/wish.model";

@Injectable({
  providedIn: 'root'
})
export class WishService {

  constructor(
    private http: HttpClient
  ) { }

  createWish(wish: WishCreation) {
    return this.http.post<Wish>('/api/wish', wish)
  }

  markAsGifted(wishID: number, gifter: string, email: string) {
    return this.http.patch<Wish>(`/api/wish/gifted?id=${wishID}&gifter=${gifter}&email=${email}`, null)
  }

  markAsNotGifted(wishID: number) {
    return this.http.patch<Wish>(`/api/wish/not-gifted?id=${wishID}`, null)
  }

  deleteWish(wishID: number, userID?: number) {
    return this.http.delete<void>(`/api/wish?id=${wishID}&userID=${userID}`)
  }
}
