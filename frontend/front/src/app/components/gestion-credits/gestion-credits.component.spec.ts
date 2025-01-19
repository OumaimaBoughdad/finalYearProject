import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionCreditsComponent } from './gestion-credits.component';

describe('GestionCreditsComponent', () => {
  let component: GestionCreditsComponent;
  let fixture: ComponentFixture<GestionCreditsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionCreditsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionCreditsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
