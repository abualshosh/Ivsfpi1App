import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComplain, getComplainIdentifier } from '../complain.model';

export type EntityResponseType = HttpResponse<IComplain>;
export type EntityArrayResponseType = HttpResponse<IComplain[]>;

@Injectable({ providedIn: 'root' })
export class ComplainService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/complains');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) { }

  create(complain: IComplain): Observable<EntityResponseType> {
    return this.http.post<IComplain>('api/complain/phones', complain, { observe: 'response' });
  }

  update(complain: IComplain): Observable<EntityResponseType> {
    return this.http.put<IComplain>(`${this.resourceUrl}/${getComplainIdentifier(complain) as number}`, complain, { observe: 'response' });
  }

  partialUpdate(complain: IComplain): Observable<EntityResponseType> {
    return this.http.patch<IComplain>(`${this.resourceUrl}/${getComplainIdentifier(complain) as number}`, complain, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComplain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComplain[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComplainToCollectionIfMissing(complainCollection: IComplain[], ...complainsToCheck: (IComplain | null | undefined)[]): IComplain[] {
    const complains: IComplain[] = complainsToCheck.filter(isPresent);
    if (complains.length > 0) {
      const complainCollectionIdentifiers = complainCollection.map(complainItem => getComplainIdentifier(complainItem)!);
      const complainsToAdd = complains.filter(complainItem => {
        const complainIdentifier = getComplainIdentifier(complainItem);
        if (complainIdentifier == null || complainCollectionIdentifiers.includes(complainIdentifier)) {
          return false;
        }
        complainCollectionIdentifiers.push(complainIdentifier);
        return true;
      });
      return [...complainsToAdd, ...complainCollection];
    }
    return complainCollection;
  }
}
