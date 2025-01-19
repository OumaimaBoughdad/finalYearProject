import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientCreditComponent } from './client-credit.component';

describe('ClientCreditComponent', () => {
  let component: ClientCreditComponent;
  let fixture: ComponentFixture<ClientCreditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientCreditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientCreditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
