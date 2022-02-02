import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPhone, Phone } from '../phone.model';
import { PhoneService } from '../service/phone.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IComplain } from 'app/entities/complain/complain.model';
import { ComplainService } from 'app/entities/complain/service/complain.service';
import { PhoneStatus } from 'app/entities/enumerations/phone-status.model';

@Component({
  selector: 'jhi-phone-update',
  templateUrl: './phone-update.component.html',
})
export class PhoneUpdateComponent implements OnInit {
  isSaving = false;
  phoneStatusValues = Object.keys(PhoneStatus);

  complainsSharedCollection: IComplain[] = [];

  editForm = this.fb.group({
    id: [],
    imei: [null, [Validators.required]],
    imei2: [],
    brand: [null, [Validators.required]],
    model: [null, [Validators.required]],
    color: [],
    descroptions: [],
    status: [],
    verifedBy: [],
    verifedDate: [],
    complain: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected phoneService: PhoneService,
    protected complainService: ComplainService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phone }) => {
      if (phone.id === undefined) {
        const today = dayjs().startOf('day');
        phone.verifedDate = today;
      }

      this.updateForm(phone);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('ivsfpi1App.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const phone = this.createFromForm();
    if (phone.id !== undefined) {
      this.subscribeToSaveResponse(this.phoneService.update(phone));
    } else {
      this.subscribeToSaveResponse(this.phoneService.create(phone));
    }
  }

  trackComplainById(index: number, item: IComplain): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhone>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(phone: IPhone): void {
    this.editForm.patchValue({
      id: phone.id,
      imei: phone.imei,
      imei2: phone.imei2,
      brand: phone.brand,
      model: phone.model,
      color: phone.color,
      descroptions: phone.descroptions,
      status: phone.status,
      verifedBy: phone.verifedBy,
      verifedDate: phone.verifedDate ? phone.verifedDate.format(DATE_TIME_FORMAT) : null,
      complain: phone.complain,
    });

    this.complainsSharedCollection = this.complainService.addComplainToCollectionIfMissing(this.complainsSharedCollection, phone.complain);
  }

  protected loadRelationshipsOptions(): void {
    this.complainService
      .query()
      .pipe(map((res: HttpResponse<IComplain[]>) => res.body ?? []))
      .pipe(
        map((complains: IComplain[]) =>
          this.complainService.addComplainToCollectionIfMissing(complains, this.editForm.get('complain')!.value)
        )
      )
      .subscribe((complains: IComplain[]) => (this.complainsSharedCollection = complains));
  }

  protected createFromForm(): IPhone {
    return {
      ...new Phone(),
      id: this.editForm.get(['id'])!.value,
      imei: this.editForm.get(['imei'])!.value,
      imei2: this.editForm.get(['imei2'])!.value,
      brand: this.editForm.get(['brand'])!.value,
      model: this.editForm.get(['model'])!.value,
      color: this.editForm.get(['color'])!.value,
      descroptions: this.editForm.get(['descroptions'])!.value,
      status: this.editForm.get(['status'])!.value,
      verifedBy: this.editForm.get(['verifedBy'])!.value,
      verifedDate: this.editForm.get(['verifedDate'])!.value
        ? dayjs(this.editForm.get(['verifedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      complain: this.editForm.get(['complain'])!.value,
    };
  }
}
