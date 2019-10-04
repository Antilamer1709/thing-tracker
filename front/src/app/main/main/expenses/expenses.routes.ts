import {Route} from "@angular/router";
import {AddExpenseComponent} from "./add-expense/add-expense.component";
import {ManageExpenseComponent} from "./manage-expense/manage-expense.component";
import {GuardService} from "../../../common/guard-service";

export const ExpensesRoutes: Route[] = [
  {
    path: 'add-expense', component: AddExpenseComponent, canActivate: [GuardService]
  },
  {
    path: 'chart', component: ManageExpenseComponent, canActivate: [GuardService]
  },
  {
    path: 'my-expenses', component: ManageExpenseComponent, canActivate: [GuardService]
  }
];
