import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ComplainComponent } from './list/complain.component';
import { ComplainDetailComponent } from './detail/complain-detail.component';
import { ComplainUpdateComponent } from './update/complain-update.component';
import { ComplainDeleteDialogComponent } from './delete/complain-delete-dialog.component';
import { ComplainRoutingModule } from './route/complain-routing.module';

@NgModule({
  imports: [SharedModule, ComplainRoutingModule],
  declarations: [ComplainComponent, ComplainDetailComponent, ComplainUpdateComponent, ComplainDeleteDialogComponent],
  entryComponents: [ComplainDeleteDialogComponent],
})
export class ComplainModule {}
