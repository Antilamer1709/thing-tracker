import {Route} from "@angular/router";
import {GuardService} from "../../../common/guard-service";
import {ProfileComponent} from "./profile/profile.component";

export const ProfileRoutes: Route[] = [
  {
    path: 'profile', component: ProfileComponent, canActivate: [GuardService]
  },
  {
    path : 'profile/:id', component : ProfileComponent, canActivate: [GuardService]
  }
];
