import { IPhone } from 'app/entities/phone/phone.model';
import { IdType } from 'app/entities/enumerations/id-type.model';

export interface IComplain {
  id?: number;
  complainNumber?: string | null;
  descpcription?: string | null;
  ownerName?: string;
  ownerPhone?: string;
  ownerID?: string;
  idType?: IdType | null;
  phones?: IPhone[] | null;
}

export class Complain implements IComplain {
  constructor(
    public id?: number,
    public complainNumber?: string | null,
    public descpcription?: string | null,
    public ownerName?: string,
    public ownerPhone?: string,
    public ownerID?: string,
    public idType?: IdType | null,
    public phones?: IPhone[] | null
  ) {}
}

export function getComplainIdentifier(complain: IComplain): number | undefined {
  return complain.id;
}
