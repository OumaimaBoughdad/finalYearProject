import { TestBed } from '@angular/core/testing';

import { ClientCreditService } from './client-credit.service';

describe('ClientCreditService', () => {
  let service: ClientCreditService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientCreditService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
