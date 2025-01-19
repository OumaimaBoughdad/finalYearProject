import { TestBed } from '@angular/core/testing';

import { CreditScoreService } from './credit-score.service';

describe('CreditScoreService', () => {
  let service: CreditScoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreditScoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
