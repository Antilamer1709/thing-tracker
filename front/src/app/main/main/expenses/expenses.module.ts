import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddExpenseComponent } from './add-expense/add-expense.component';
import {
  AutoCompleteModule,
  ButtonModule,
  ChartModule,
  InputTextModule, MultiSelectModule,
  SpinnerModule
} from "primeng";
import { CalendarModule } from "primeng/calendar";
import {FormsModule} from "@angular/forms";
import { ExpensesChartComponent } from './expenses-chart/expenses-chart.component';
import {CommonSharedModule} from "../../../common/common-shared.module";
import { MyExpensesComponent } from './my-expenses/my-expenses.component';
import {ProfileModule} from "../profile/profile.module";

@NgModule({
  declarations: [AddExpenseComponent, ExpensesChartComponent, MyExpensesComponent],
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
    MultiSelectModule,
    ProfileModule
  ]
})
export class ExpensesModule { }
