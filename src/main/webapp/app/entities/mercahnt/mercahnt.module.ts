import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MercahntComponent } from './list/mercahnt.component';
import { MercahntDetailComponent } from './detail/mercahnt-detail.component';
import { MercahntUpdateComponent } from './update/mercahnt-update.component';
import { MercahntDeleteDialogComponent } from './delete/mercahnt-delete-dialog.component';
import { MercahntRoutingModule } from './route/mercahnt-routing.module';

@NgModule({
  imports: [SharedModule, MercahntRoutingModule],
  declarations: [MercahntComponent, MercahntDetailComponent, MercahntUpdateComponent, MercahntDeleteDialogComponent],
  entryComponents: [MercahntDeleteDialogComponent],
})
export class MercahntModule {}
