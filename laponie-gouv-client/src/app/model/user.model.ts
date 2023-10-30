import {Family} from "./family.model";
import {Wish} from "./wish.model";

export interface User {
  id: number;
  email: string;
  name?: string;
  family: Family[];
  wishes: Wish[];
}

export interface UserCreation {
  email: string;
  name?: string;
}
