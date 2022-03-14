import { IdType } from "../enumerations/id-type.model"
import { IPhone } from "../phone/phone.model"

export interface IComplain {
  descpcription?: string | null;
  ownerName?: string;
  ownerPhone?: string;
  ownerID?: string;
  idType?: IdType | null;
  phones?: IPhone[] | null;
}
export class ComplainPhoneDto {
  constructor(
    public descpcription?: string | null,
    public ownerName?: string,
    public ownerPhone?: string,
    public ownerID?: string,
    public idType?: IdType | null,
    public phones?: IPhone[] | null
  ) { }
}
