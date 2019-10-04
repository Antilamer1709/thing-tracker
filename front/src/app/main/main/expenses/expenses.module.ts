import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddExpenseComponent } from './add-expense/add-expense.component';
import {
  AutoCompleteModule,
  ButtonModule,
  CalendarModule,
  ChartModule,
  InputTextModule, MultiSelectModule,
  SpinnerModule
} from "primeng/primeng";
import {FormsModule} from "@angular/forms";
import { ExpensesChartComponent } from './expenses-chart/expenses-chart.component';
import {CommonSharedModule} from "../../../common/common-shared.module";

@NgModule({
  declarations: [AddExpenseComponent, ExpensesChartComponent],
  imports: [
    CommonModule,
    FormsModule,
    AutoCompleteModule,
    SpinnerModule,
    ButtonModule,
    InputTextModule,
    CalendarModule,
    ChartModule,
    CommonSharedModule,
    MultiSelectModule
  ]
})
export class ExpensesModule { }
