import { TestBed } from '@angular/core/testing';

import { ExpensesService } from './expenses.service';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ExpensesService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      RouterTestingModule,
      HttpClientTestingModule,
    ]
  }));

  it('should be created', () => {
    const service: ExpensesService = TestBed.get(ExpensesService);
    expect(service).toBeTruthy();
  });
});
