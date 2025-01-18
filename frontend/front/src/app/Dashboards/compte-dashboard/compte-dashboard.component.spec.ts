import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompteDashboardComponent } from './compte-dashboard.component';

describe('CompteDashboardComponent', () => {
  let component: CompteDashboardComponent;
  let fixture: ComponentFixture<CompteDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompteDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompteDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
