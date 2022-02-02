import { IUser } from 'app/entities/user/user.model';

export interface IMercahnt {
  id?: number;
  name?: string | null;
  address?: string | null;
  phoneNumber?: string | null;
  user?: IUser | null;
}

export class Mercahnt implements IMercahnt {
  constructor(
    public id?: number,
    public name?: string | null,
    public address?: string | null,
    public phoneNumber?: string | null,
    public user?: IUser | null
  ) {}
}

export function getMercahntIdentifier(mercahnt: IMercahnt): number | undefined {
  return mercahnt.id;
}
