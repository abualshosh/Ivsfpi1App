import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMercahnt } from '../mercahnt.model';
import { MercahntService } from '../service/mercahnt.service';

@Component({
  templateUrl: './mercahnt-delete-dialog.component.html',
})
export class MercahntDeleteDialogComponent {
  mercahnt?: IMercahnt;

  constructor(protected mercahntService: MercahntService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mercahntService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
