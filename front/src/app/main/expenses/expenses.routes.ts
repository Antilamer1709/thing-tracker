import {Route} from "@angular/router";
import {AddExpenseComponent} from "./add-expense/add-expense.component";
import {ManageExpenseComponent} from "./manage-expense/manage-expense.component";

export const ExpensesRoutes: Route[] = [
  {
    path: 'add-expense', component: AddExpenseComponent
  },
  {
    path: 'manage-expense', component: ManageExpenseComponent
  }
];
