import {Route} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ExpensesRoutes} from "./expenses/expenses.routes";

export const MainRoutes: Route[] = [
  {
    path: 'main', component: MainComponent, children: [...ExpensesRoutes]
  }
];
