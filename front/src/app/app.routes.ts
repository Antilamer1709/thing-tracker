import {Routes} from "@angular/router";
import {AuthenticationRoutes} from "./authentication/authentication.routes";

export const routes: Routes = [

  ...AuthenticationRoutes,

  { path: '**',
    redirectTo: '/'
  }

];
