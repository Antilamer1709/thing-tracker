/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { AuthRepository } from './auth.repository';

describe('Repository: Auth', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthRepository]
    });
  });

  it('should ...', inject([AuthRepository], (repository: AuthRepository) => {
    expect(repository).toBeTruthy();
  }));
});
