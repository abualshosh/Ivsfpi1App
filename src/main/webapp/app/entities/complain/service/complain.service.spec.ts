import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IdType } from 'app/entities/enumerations/id-type.model';
import { IComplain, Complain } from '../complain.model';

import { ComplainService } from './complain.service';

describe('Complain Service', () => {
  let service: ComplainService;
  let httpMock: HttpTestingController;
  let elemDefault: IComplain;
  let expectedResult: IComplain | IComplain[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComplainService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      complainNumber: 'AAAAAAA',
      descpcription: 'AAAAAAA',
      ownerName: 'AAAAAAA',
      ownerPhone: 'AAAAAAA',
      ownerID: 'AAAAAAA',
      idType: IdType.DERIVER,
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

    it('should create a Complain', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Complain()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Complain', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          complainNumber: 'BBBBBB',
          descpcription: 'BBBBBB',
          ownerName: 'BBBBBB',
          ownerPhone: 'BBBBBB',
          ownerID: 'BBBBBB',
          idType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Complain', () => {
      const patchObject = Object.assign(
        {
          descpcription: 'BBBBBB',
          ownerName: 'BBBBBB',
          ownerID: 'BBBBBB',
          idType: 'BBBBBB',
        },
        new Complain()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Complain', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          complainNumber: 'BBBBBB',
          descpcription: 'BBBBBB',
          ownerName: 'BBBBBB',
          ownerPhone: 'BBBBBB',
          ownerID: 'BBBBBB',
          idType: 'BBBBBB',
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

    it('should delete a Complain', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComplainToCollectionIfMissing', () => {
      it('should add a Complain to an empty array', () => {
        const complain: IComplain = { id: 123 };
        expectedResult = service.addComplainToCollectionIfMissing([], complain);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(complain);
      });

      it('should not add a Complain to an array that contains it', () => {
        const complain: IComplain = { id: 123 };
        const complainCollection: IComplain[] = [
          {
            ...complain,
          },
          { id: 456 },
        ];
        expectedResult = service.addComplainToCollectionIfMissing(complainCollection, complain);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Complain to an array that doesn't contain it", () => {
        const complain: IComplain = { id: 123 };
        const complainCollection: IComplain[] = [{ id: 456 }];
        expectedResult = service.addComplainToCollectionIfMissing(complainCollection, complain);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(complain);
      });

      it('should add only unique Complain to an array', () => {
        const complainArray: IComplain[] = [{ id: 123 }, { id: 456 }, { id: 21219 }];
        const complainCollection: IComplain[] = [{ id: 123 }];
        expectedResult = service.addComplainToCollectionIfMissing(complainCollection, ...complainArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const complain: IComplain = { id: 123 };
        const complain2: IComplain = { id: 456 };
        expectedResult = service.addComplainToCollectionIfMissing([], complain, complain2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(complain);
        expect(expectedResult).toContain(complain2);
      });

      it('should accept null and undefined values', () => {
        const complain: IComplain = { id: 123 };
        expectedResult = service.addComplainToCollectionIfMissing([], null, complain, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(complain);
      });

      it('should return initial array if no Complain is added', () => {
        const complainCollection: IComplain[] = [{ id: 123 }];
        expectedResult = service.addComplainToCollectionIfMissing(complainCollection, undefined, null);
        expect(expectedResult).toEqual(complainCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
