import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMercahnt, getMercahntIdentifier } from '../mercahnt.model';

export type EntityResponseType = HttpResponse<IMercahnt>;
export type EntityArrayResponseType = HttpResponse<IMercahnt[]>;

@Injectable({ providedIn: 'root' })
export class MercahntService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mercahnts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mercahnt: IMercahnt): Observable<EntityResponseType> {
    return this.http.post<IMercahnt>(this.resourceUrl, mercahnt, { observe: 'response' });
  }

  update(mercahnt: IMercahnt): Observable<EntityResponseType> {
    return this.http.put<IMercahnt>(`${this.resourceUrl}/${getMercahntIdentifier(mercahnt) as number}`, mercahnt, { observe: 'response' });
  }

  partialUpdate(mercahnt: IMercahnt): Observable<EntityResponseType> {
    return this.http.patch<IMercahnt>(`${this.resourceUrl}/${getMercahntIdentifier(mercahnt) as number}`, mercahnt, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMercahnt>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMercahnt[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMercahntToCollectionIfMissing(mercahntCollection: IMercahnt[], ...mercahntsToCheck: (IMercahnt | null | undefined)[]): IMercahnt[] {
    const mercahnts: IMercahnt[] = mercahntsToCheck.filter(isPresent);
    if (mercahnts.length > 0) {
      const mercahntCollectionIdentifiers = mercahntCollection.map(mercahntItem => getMercahntIdentifier(mercahntItem)!);
      const mercahntsToAdd = mercahnts.filter(mercahntItem => {
        const mercahntIdentifier = getMercahntIdentifier(mercahntItem);
        if (mercahntIdentifier == null || mercahntCollectionIdentifiers.includes(mercahntIdentifier)) {
          return false;
        }
        mercahntCollectionIdentifiers.push(mercahntIdentifier);
        return true;
      });
      return [...mercahntsToAdd, ...mercahntCollection];
    }
    return mercahntCollection;
  }
}
