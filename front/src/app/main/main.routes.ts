import {Route} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ExpensesRoutes} from "./main/expenses/expenses.routes";
import {DashboardComponent} from "./main/dashboard/dashboard.component";
import {MyGroupRoutes} from "./main/user/user.routes";

export const MainRoutes: Route[] = [
  {
    path: 'main', component: MainComponent, children: [
      ...ExpensesRoutes,
      ...MyGroupRoutes,
      {
        path: 'dashboard', component: DashboardComponent
      }]
  }
];
