import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComplainService } from '../service/complain.service';
import { IComplain, Complain } from '../complain.model';

import { ComplainUpdateComponent } from './complain-update.component';

describe('Complain Management Update Component', () => {
  let comp: ComplainUpdateComponent;
  let fixture: ComponentFixture<ComplainUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let complainService: ComplainService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComplainUpdateComponent],
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
      .overrideTemplate(ComplainUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComplainUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    complainService = TestBed.inject(ComplainService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const complain: IComplain = { id: 456 };

      activatedRoute.data = of({ complain });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(complain));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Complain>>();
      const complain = { id: 123 };
      jest.spyOn(complainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ complain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: complain }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(complainService.update).toHaveBeenCalledWith(complain);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Complain>>();
      const complain = new Complain();
      jest.spyOn(complainService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ complain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: complain }));
      saveSubject.complete();

      // THEN
      expect(complainService.create).toHaveBeenCalledWith(complain);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Complain>>();
      const complain = { id: 123 };
      jest.spyOn(complainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ complain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(complainService.update).toHaveBeenCalledWith(complain);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
