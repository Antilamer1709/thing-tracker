import {Route} from "@angular/router";
import {GuardService} from "../../../common/guard-service";
import {AddToGroupComponent} from "./add-to-group/add-to-group.component";

export const UserRoutes: Route[] = [
  {
    path: 'add-to-group', component: AddToGroupComponent, canActivate: [GuardService]
  }
];
