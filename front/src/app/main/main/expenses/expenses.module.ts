import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddExpenseComponent } from './add-expense/add-expense.component';
import {
  AutoCompleteModule,
  ButtonModule,
  CalendarModule,
  ChartModule,
  InputTextModule,
  SpinnerModule
} from "primeng/primeng";
import {FormsModule} from "@angular/forms";
import { ManageExpenseComponent } from './manage-expense/manage-expense.component';
import {CommonSharedModule} from "../../../common/common-shared.module";

@NgModule({
  declarations: [AddExpenseComponent, ManageExpenseComponent],
  imports: [
    CommonModule,
    FormsModule,
    AutoCompleteModule,
    SpinnerModule,
    ButtonModule,
    InputTextModule,
    CalendarModule,
    ChartModule,
    CommonSharedModule
  ]
})
export class ExpensesModule { }
