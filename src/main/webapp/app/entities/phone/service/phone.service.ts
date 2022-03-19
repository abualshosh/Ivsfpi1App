import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPhone, getPhoneIdentifier } from '../phone.model';

export type EntityResponseType = HttpResponse<IPhone>;
export type EntityArrayResponseType = HttpResponse<IPhone[]>;

@Injectable({ providedIn: 'root' })
export class PhoneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/phones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) { }

  create(phone: IPhone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phone);
    return this.http
      .post<IPhone>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(phone: IPhone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phone);
    return this.http
      .put<IPhone>(`${this.resourceUrl}/${getPhoneIdentifier(phone) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(phone: IPhone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phone);
    return this.http
      .patch<IPhone>(`${this.resourceUrl}/${getPhoneIdentifier(phone) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPhone>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  getPhone(imei: string): Observable<EntityResponseType> {
    return this.http
      .get<IPhone>('api/phone' + '/' + imei, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPhone[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPhoneToCollectionIfMissing(phoneCollection: IPhone[], ...phonesToCheck: (IPhone | null | undefined)[]): IPhone[] {
    const phones: IPhone[] = phonesToCheck.filter(isPresent);
    if (phones.length > 0) {
      const phoneCollectionIdentifiers = phoneCollection.map(phoneItem => getPhoneIdentifier(phoneItem)!);
      const phonesToAdd = phones.filter(phoneItem => {
        const phoneIdentifier = getPhoneIdentifier(phoneItem);
        if (phoneIdentifier == null || phoneCollectionIdentifiers.includes(phoneIdentifier)) {
          return false;
        }
        phoneCollectionIdentifiers.push(phoneIdentifier);
        return true;
      });
      return [...phonesToAdd, ...phoneCollection];
    }
    return phoneCollection;
  }

  protected convertDateFromClient(phone: IPhone): IPhone {
    return Object.assign({}, phone, {
      verifedDate: phone.verifedDate?.isValid() ? phone.verifedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.verifedDate = res.body.verifedDate ? dayjs(res.body.verifedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((phone: IPhone) => {
        phone.verifedDate = phone.verifedDate ? dayjs(phone.verifedDate) : undefined;
      });
    }
    return res;
  }
}
