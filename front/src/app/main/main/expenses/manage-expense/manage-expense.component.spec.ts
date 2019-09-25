import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageExpenseComponent } from './manage-expense.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MessageService} from "primeng/api";
import {AutoCompleteModule, CalendarModule, ChartModule, MultiSelectModule} from "primeng/primeng";

describe('ManageExpenseComponent', () => {
  let component: ManageExpenseComponent;
  let fixture: ComponentFixture<ManageExpenseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,
        CalendarModule,
        MultiSelectModule,
        AutoCompleteModule,
        ChartModule,

        HttpClientTestingModule
      ],
      declarations: [ManageExpenseComponent],
      providers: [
        MessageService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageExpenseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
