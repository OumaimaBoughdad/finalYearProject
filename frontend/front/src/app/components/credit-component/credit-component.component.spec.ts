import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreditComponentComponent } from './credit-component.component';

describe('CreditComponentComponent', () => {
  let component: CreditComponentComponent;
  let fixture: ComponentFixture<CreditComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreditComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreditComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
