import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IComplain, Complain } from '../complain.model';
import { ComplainService } from '../service/complain.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-complain-update',
  templateUrl: './complain-update.component.html',
})
export class ComplainUpdateComponent implements OnInit {
  isSaving = false;


  editForm = this.fb.group({
    id: [],
    complainNumber: [],
    descpcription: [],
    ownerName: [null, [Validators.required]],
    ownerPhone: [null, [Validators.required]],
    ownerID: [null, [Validators.required]],
    idType: [],
    phones: this.fb.array([]),

  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected complainService: ComplainService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) { }


  phones(): FormArray {
    return this.editForm.get('phones') as FormArray;
  }
  newPhone(): FormGroup {
    return this.fb.group({
      imei: [],
      imei2: [],
      brand: [],
      model: [],
      color: [],
    })

  }
  addPhone(): void {
    this.phones().push(this.newPhone());
  }

  removePhone(i: number): void {
    this.phones().removeAt(i);
  }
  ngOnInit(): void {
    this.editForm.get("idType")?.valueChanges.subscribe(v => {
      console.log(v);

    })
    // console.log();

    this.activatedRoute.data.subscribe(({ complain }) => {
      this.updateForm(complain);
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
    const complain = this.createFromForm();

    if (complain.id !== undefined) {
      this.subscribeToSaveResponse(this.complainService.update(complain));
    } else {
      this.subscribeToSaveResponse(this.complainService.create(complain));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComplain>>): void {
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

  protected updateForm(complain: IComplain): void {
    this.editForm.patchValue({
      id: complain.id,
      descpcription: complain.descpcription,
      ownerName: complain.ownerName,
      ownerPhone: complain.ownerPhone,
      ownerID: complain.ownerID,
      idType: complain.idType,
    });
  }

  protected createFromForm(): IComplain {
    return {
      ...new Complain(),
      descpcription: this.editForm.get(['descpcription'])!.value,
      ownerName: this.editForm.get(['ownerName'])!.value,
      ownerPhone: this.editForm.get(['ownerPhone'])!.value,
      ownerID: this.editForm.get(['ownerID'])!.value,
      idType: this.editForm.get(['idType'])!.value,
      phones: this.editForm.get(['phones'])?.value
    };
  }
}
