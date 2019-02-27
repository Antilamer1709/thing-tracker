import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddExpenseComponent } from './add-expense/add-expense.component';
import {AutoCompleteModule} from "primeng/primeng";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [AddExpenseComponent],
  imports: [
    CommonModule,
    FormsModule,
    AutoCompleteModule
  ]
})
export class ExpensesModule { }
