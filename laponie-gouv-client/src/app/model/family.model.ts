import {User} from "./user.model";
import {Kid} from "./kid.model";

export interface Family {
  id: number;
  name: string;
  users: User[];
  kids: Kid[];
}

export interface FamilyCreation {
  email: string;
  name: string;
}
