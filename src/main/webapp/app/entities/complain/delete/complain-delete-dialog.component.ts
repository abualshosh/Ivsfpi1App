import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComplain } from '../complain.model';
import { ComplainService } from '../service/complain.service';

@Component({
  templateUrl: './complain-delete-dialog.component.html',
})
export class ComplainDeleteDialogComponent {
  complain?: IComplain;

  constructor(protected complainService: ComplainService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.complainService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
