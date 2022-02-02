import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MercahntComponent } from '../list/mercahnt.component';
import { MercahntDetailComponent } from '../detail/mercahnt-detail.component';
import { MercahntUpdateComponent } from '../update/mercahnt-update.component';
import { MercahntRoutingResolveService } from './mercahnt-routing-resolve.service';

const mercahntRoute: Routes = [
  {
    path: '',
    component: MercahntComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MercahntDetailComponent,
    resolve: {
      mercahnt: MercahntRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MercahntUpdateComponent,
    resolve: {
      mercahnt: MercahntRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MercahntUpdateComponent,
    resolve: {
      mercahnt: MercahntRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mercahntRoute)],
  exports: [RouterModule],
})
export class MercahntRoutingModule {}
