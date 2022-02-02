import dayjs from 'dayjs/esm';
import { IComplain } from 'app/entities/complain/complain.model';
import { PhoneStatus } from 'app/entities/enumerations/phone-status.model';

export interface IPhone {
  id?: number;
  imei?: string;
  imei2?: string | null;
  brand?: string;
  model?: string;
  color?: string | null;
  descroptions?: string | null;
  status?: PhoneStatus | null;
  verifedBy?: string | null;
  verifedDate?: dayjs.Dayjs | null;
  complain?: IComplain | null;
}

export class Phone implements IPhone {
  constructor(
    public id?: number,
    public imei?: string,
    public imei2?: string | null,
    public brand?: string,
    public model?: string,
    public color?: string | null,
    public descroptions?: string | null,
    public status?: PhoneStatus | null,
    public verifedBy?: string | null,
    public verifedDate?: dayjs.Dayjs | null,
    public complain?: IComplain | null
  ) {}
}

export function getPhoneIdentifier(phone: IPhone): number | undefined {
  return phone.id;
}
