import {Route} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ExpensesRoutes} from "./main/expenses/expenses.routes";
import {DashboardComponent} from "./main/dashboard/dashboard.component";
import {UserRoutes} from "./main/user/user.routes";
import {GroupRoutes} from "./main/group/group.routes";

export const MainRoutes: Route[] = [
  {
    path: 'main', component: MainComponent, children: [
      ...ExpensesRoutes,
      ...UserRoutes,
      ...GroupRoutes,
      {
        path: 'dashboard', component: DashboardComponent
      }]
  }
];
