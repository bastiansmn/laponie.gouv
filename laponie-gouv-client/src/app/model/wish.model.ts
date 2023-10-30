import {Interpolation} from "@angular/compiler";

export interface Wish {
  id: number;
  link: string;
  gifted: boolean;
  name?: string;
  price?: number;
  comment?: string;
}

export interface WishCreation {
  email: string;
  link: string;
  name?: string;
  price?: number;
  comment?: string;
}
