import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMercahnt, Mercahnt } from '../mercahnt.model';
import { MercahntService } from '../service/mercahnt.service';

import { MercahntRoutingResolveService } from './mercahnt-routing-resolve.service';

describe('Mercahnt routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MercahntRoutingResolveService;
  let service: MercahntService;
  let resultMercahnt: IMercahnt | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(MercahntRoutingResolveService);
    service = TestBed.inject(MercahntService);
    resultMercahnt = undefined;
  });

  describe('resolve', () => {
    it('should return IMercahnt returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMercahnt = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMercahnt).toEqual({ id: 123 });
    });

    it('should return new IMercahnt if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMercahnt = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMercahnt).toEqual(new Mercahnt());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mercahnt })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMercahnt = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMercahnt).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
