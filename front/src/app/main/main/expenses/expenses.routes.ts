import {Route} from "@angular/router";
import {AddExpenseComponent} from "./add-expense/add-expense.component";
import {ExpensesChartComponent} from "./expenses-chart/expenses-chart.component";
import {GuardService} from "../../../common/guard-service";

export const ExpensesRoutes: Route[] = [
  {
    path: 'add-expense', component: AddExpenseComponent, canActivate: [GuardService]
  },
  {
    path: 'chart', component: ExpensesChartComponent, canActivate: [GuardService]
  },
  {
    path: 'my-expenses', component: ExpensesChartComponent, canActivate: [GuardService]
  }
];
