export interface IMerchantUserDto {
  email: string,
  firstName: string | null,
  lastName: string | null,
  login: string,
  address?: string | null,
  phoneNumber?: string | null,
}
export class MerchantUserDto {
  constructor(
    public email: string,
    public firstName: string | null,
    public lastName: string | null,
    public login: string,
    public address?: string | null,
    public phoneNumber?: string | null,
  ) { }
}
