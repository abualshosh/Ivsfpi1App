import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MercahntDetailComponent } from './mercahnt-detail.component';

describe('Mercahnt Management Detail Component', () => {
  let comp: MercahntDetailComponent;
  let fixture: ComponentFixture<MercahntDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MercahntDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mercahnt: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MercahntDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MercahntDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mercahnt on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mercahnt).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
