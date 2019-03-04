import {Routes} from "@angular/router";
import {AuthenticationRoutes} from "./authentication/authentication.routes";
import {MainRoutes} from "./main/main.routes";

export const routes: Routes = [

  ...AuthenticationRoutes,
  ...MainRoutes,

  { path: '**',
    redirectTo: '/main/dashboard'
  }

];
