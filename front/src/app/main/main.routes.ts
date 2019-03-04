import {Route} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ExpensesRoutes} from "./expenses/expenses.routes";
import {DashboardComponent} from "./dashboard/dashboard.component";

export const MainRoutes: Route[] = [
  {
    path: 'main', component: MainComponent, children: [
      ...ExpensesRoutes,
      {
        path: 'dashboard', component: DashboardComponent
      }]
  }
];
