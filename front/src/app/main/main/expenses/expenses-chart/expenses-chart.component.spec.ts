import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpensesChartComponent } from './expenses-chart.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MessageService} from "primeng/api";
import {AutoCompleteModule, ChartModule, MultiSelectModule} from "primeng";
import { CalendarModule } from "primeng/calendar";

describe('ExpensesChartComponent', () => {
  let component: ExpensesChartComponent;
  let fixture: ComponentFixture<ExpensesChartComponent>;

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
      declarations: [ExpensesChartComponent],
      providers: [
        MessageService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpensesChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
