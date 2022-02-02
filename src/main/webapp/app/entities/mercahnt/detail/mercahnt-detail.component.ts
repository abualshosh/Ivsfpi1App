import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMercahnt } from '../mercahnt.model';

@Component({
  selector: 'jhi-mercahnt-detail',
  templateUrl: './mercahnt-detail.component.html',
})
export class MercahntDetailComponent implements OnInit {
  mercahnt: IMercahnt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mercahnt }) => {
      this.mercahnt = mercahnt;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
