import { TestBed } from '@angular/core/testing';

import { MainMenuService } from './main-menu.service';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('MainMenuService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      RouterTestingModule,
      HttpClientTestingModule,
    ]
  }));

  it('should be created', () => {
    const service: MainMenuService = TestBed.get(MainMenuService);
    expect(service).toBeTruthy();
  });
});
