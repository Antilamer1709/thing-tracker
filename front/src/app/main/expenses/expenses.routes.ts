import {Route} from "@angular/router";
import {AddExpenseComponent} from "./add-expense/add-expense.component";

export const ExpensesRoutes: Route[] = [
  {
    path: 'add-expense', component: AddExpenseComponent
  }
];
