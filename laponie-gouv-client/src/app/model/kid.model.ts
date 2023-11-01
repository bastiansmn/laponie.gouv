import {Wish} from "./wish.model";

export interface Kid {
  id: number;
  name: string;
  wishes: Wish[];
}
