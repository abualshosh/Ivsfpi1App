import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComplain, Complain } from '../complain.model';
import { ComplainService } from '../service/complain.service';

@Injectable({ providedIn: 'root' })
export class ComplainRoutingResolveService implements Resolve<IComplain> {
  constructor(protected service: ComplainService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComplain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((complain: HttpResponse<Complain>) => {
          if (complain.body) {
            return of(complain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Complain());
  }
}
