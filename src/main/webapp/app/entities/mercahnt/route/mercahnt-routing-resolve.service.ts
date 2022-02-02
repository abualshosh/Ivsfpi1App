import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMercahnt, Mercahnt } from '../mercahnt.model';
import { MercahntService } from '../service/mercahnt.service';

@Injectable({ providedIn: 'root' })
export class MercahntRoutingResolveService implements Resolve<IMercahnt> {
  constructor(protected service: MercahntService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMercahnt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mercahnt: HttpResponse<Mercahnt>) => {
          if (mercahnt.body) {
            return of(mercahnt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mercahnt());
  }
}
