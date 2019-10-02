import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileExpensesComponent } from './profile-expenses.component';

describe('ProfileExpensesComponent', () => {
  let component: ProfileExpensesComponent;
  let fixture: ComponentFixture<ProfileExpensesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProfileExpensesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileExpensesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
