import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PhoneService } from '../service/phone.service';
import { IPhone, Phone } from '../phone.model';
import { IComplain } from 'app/entities/complain/complain.model';
import { ComplainService } from 'app/entities/complain/service/complain.service';

import { PhoneUpdateComponent } from './phone-update.component';

describe('Phone Management Update Component', () => {
  let comp: PhoneUpdateComponent;
  let fixture: ComponentFixture<PhoneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let phoneService: PhoneService;
  let complainService: ComplainService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PhoneUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PhoneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PhoneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    phoneService = TestBed.inject(PhoneService);
    complainService = TestBed.inject(ComplainService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Complain query and add missing value', () => {
      const phone: IPhone = { id: 456 };
      const complain: IComplain = { id: 24844 };
      phone.complain = complain;

      const complainCollection: IComplain[] = [{ id: 98552 }];
      jest.spyOn(complainService, 'query').mockReturnValue(of(new HttpResponse({ body: complainCollection })));
      const additionalComplains = [complain];
      const expectedCollection: IComplain[] = [...additionalComplains, ...complainCollection];
      jest.spyOn(complainService, 'addComplainToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      expect(complainService.query).toHaveBeenCalled();
      expect(complainService.addComplainToCollectionIfMissing).toHaveBeenCalledWith(complainCollection, ...additionalComplains);
      expect(comp.complainsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const phone: IPhone = { id: 456 };
      const complain: IComplain = { id: 93281 };
      phone.complain = complain;

      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(phone));
      expect(comp.complainsSharedCollection).toContain(complain);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Phone>>();
      const phone = { id: 123 };
      jest.spyOn(phoneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: phone }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(phoneService.update).toHaveBeenCalledWith(phone);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Phone>>();
      const phone = new Phone();
      jest.spyOn(phoneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: phone }));
      saveSubject.complete();

      // THEN
      expect(phoneService.create).toHaveBeenCalledWith(phone);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Phone>>();
      const phone = { id: 123 };
      jest.spyOn(phoneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(phoneService.update).toHaveBeenCalledWith(phone);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackComplainById', () => {
      it('Should return tracked Complain primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackComplainById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
