import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMercahnt, Mercahnt } from '../mercahnt.model';

import { MercahntService } from './mercahnt.service';

describe('Mercahnt Service', () => {
  let service: MercahntService;
  let httpMock: HttpTestingController;
  let elemDefault: IMercahnt;
  let expectedResult: IMercahnt | IMercahnt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MercahntService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      address: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Mercahnt', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Mercahnt()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mercahnt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
          phoneNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mercahnt', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Mercahnt()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mercahnt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
          phoneNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Mercahnt', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMercahntToCollectionIfMissing', () => {
      it('should add a Mercahnt to an empty array', () => {
        const mercahnt: IMercahnt = { id: 123 };
        expectedResult = service.addMercahntToCollectionIfMissing([], mercahnt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mercahnt);
      });

      it('should not add a Mercahnt to an array that contains it', () => {
        const mercahnt: IMercahnt = { id: 123 };
        const mercahntCollection: IMercahnt[] = [
          {
            ...mercahnt,
          },
          { id: 456 },
        ];
        expectedResult = service.addMercahntToCollectionIfMissing(mercahntCollection, mercahnt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mercahnt to an array that doesn't contain it", () => {
        const mercahnt: IMercahnt = { id: 123 };
        const mercahntCollection: IMercahnt[] = [{ id: 456 }];
        expectedResult = service.addMercahntToCollectionIfMissing(mercahntCollection, mercahnt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mercahnt);
      });

      it('should add only unique Mercahnt to an array', () => {
        const mercahntArray: IMercahnt[] = [{ id: 123 }, { id: 456 }, { id: 15964 }];
        const mercahntCollection: IMercahnt[] = [{ id: 123 }];
        expectedResult = service.addMercahntToCollectionIfMissing(mercahntCollection, ...mercahntArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mercahnt: IMercahnt = { id: 123 };
        const mercahnt2: IMercahnt = { id: 456 };
        expectedResult = service.addMercahntToCollectionIfMissing([], mercahnt, mercahnt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mercahnt);
        expect(expectedResult).toContain(mercahnt2);
      });

      it('should accept null and undefined values', () => {
        const mercahnt: IMercahnt = { id: 123 };
        expectedResult = service.addMercahntToCollectionIfMissing([], null, mercahnt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mercahnt);
      });

      it('should return initial array if no Mercahnt is added', () => {
        const mercahntCollection: IMercahnt[] = [{ id: 123 }];
        expectedResult = service.addMercahntToCollectionIfMissing(mercahntCollection, undefined, null);
        expect(expectedResult).toEqual(mercahntCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
