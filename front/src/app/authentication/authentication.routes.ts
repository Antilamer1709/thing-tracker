import {Route} from "@angular/router";
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";

export const AuthenticationRoutes: Route[] = [
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'login/:token', component: LoginComponent
  },
  {
    path: 'registration', component: RegistrationComponent
  },
];
