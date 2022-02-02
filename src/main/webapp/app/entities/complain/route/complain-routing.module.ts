import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComplainComponent } from '../list/complain.component';
import { ComplainDetailComponent } from '../detail/complain-detail.component';
import { ComplainUpdateComponent } from '../update/complain-update.component';
import { ComplainRoutingResolveService } from './complain-routing-resolve.service';

const complainRoute: Routes = [
  {
    path: '',
    component: ComplainComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComplainDetailComponent,
    resolve: {
      complain: ComplainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComplainUpdateComponent,
    resolve: {
      complain: ComplainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComplainUpdateComponent,
    resolve: {
      complain: ComplainRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(complainRoute)],
  exports: [RouterModule],
})
export class ComplainRoutingModule {}
