import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMercahnt, Mercahnt } from '../mercahnt.model';
import { MercahntService } from '../service/mercahnt.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-mercahnt-update',
  templateUrl: './mercahnt-update.component.html',
})
export class MercahntUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    address: [],
    phoneNumber: [],
    user: [],
  });

  constructor(
    protected mercahntService: MercahntService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mercahnt }) => {
      this.updateForm(mercahnt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mercahnt = this.createFromForm();
    if (mercahnt.id !== undefined) {
      this.subscribeToSaveResponse(this.mercahntService.update(mercahnt));
    } else {
      this.subscribeToSaveResponse(this.mercahntService.create(mercahnt));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMercahnt>>): void {
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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, mercahnt.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IMercahnt {
    return {
      ...new Mercahnt(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
