import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPhone } from '../phone.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-phone-detail',
  templateUrl: './phone-detail.component.html',
})
export class PhoneDetailComponent implements OnInit {
  phone: IPhone | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phone }) => {
      this.phone = phone;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
