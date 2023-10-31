import {User} from "./user.model";

export interface Family {
  id: number;
  name: string;
  users: User[];
}

export interface FamilyCreation {
  email: string;
  name: string;
}
