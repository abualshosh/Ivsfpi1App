import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMercahnt } from '../mercahnt.model';
import { MercahntService } from '../service/mercahnt.service';
import { IUser } from 'app/entities/user/user.model';
import { IMerchantUserDto, MerchantUserDto } from './merchantUserDTO.model';

@Component({
  selector: 'jhi-mercahnt-update',
  templateUrl: './mercahnt-update.component.html',
})
export class MercahntUpdateComponent implements OnInit {
  isSaving = false;
  phoneNumberRegex = /^(?:\+249|00249|249|0)?(\d{9})$/;




  editForm = this.fb.group({
    address: ['', Validators.required],
    phoneNumber: [
      '',
      [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(10),
        Validators.pattern(this.phoneNumberRegex),
      ],
    ],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', Validators.required],

  });

  constructor(
    protected mercahntService: MercahntService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mercahnt }) => {
      this.updateForm(mercahnt);

    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mercahnt = this.createFromForm();
    // if (mercahnt.id !== undefined) {
    //   this.subscribeToSaveResponse(this.mercahntService.update(mercahnt));
    // } else {
    this.subscribeToSaveResponse(this.mercahntService.createWithUser(mercahnt));
    // }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMerchantUserDto>>): void {
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

  protected updateForm(mercahnt: IMercahnt): void {
    this.editForm.patchValue({
      id: mercahnt.id,
      name: mercahnt.name,
      address: mercahnt.address,
      phoneNumber: mercahnt.phoneNumber,
      user: mercahnt.user,
    });

  }



  protected createFromForm(): IMerchantUserDto {
    return {
      ...new MerchantUserDto(
        this.editForm.get(['email'])?.value,
        this.editForm.get(['firstName'])?.value,
        this.editForm.get(['lastName'])?.value,
        this.editForm.get(['phoneNumber'])?.value,
        this.editForm.get(['address'])?.value,
        this.editForm.get(['phoneNumber'])?.value,
      ),
    };
  }
}
