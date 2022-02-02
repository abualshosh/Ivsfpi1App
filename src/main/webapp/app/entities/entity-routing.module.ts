import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'complain',
        data: { pageTitle: 'Complains' },
        loadChildren: () => import('./complain/complain.module').then(m => m.ComplainModule),
      },
      {
        path: 'phone',
        data: { pageTitle: 'Phones' },
        loadChildren: () => import('./phone/phone.module').then(m => m.PhoneModule),
      },
      {
        path: 'mercahnt',
        data: { pageTitle: 'Mercahnts' },
        loadChildren: () => import('./mercahnt/mercahnt.module').then(m => m.MercahntModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
