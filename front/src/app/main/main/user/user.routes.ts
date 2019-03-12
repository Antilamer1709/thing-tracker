import {Route} from "@angular/router";
import {GuardService} from "../../../common/guard-service";
import {MyGroupComponent} from "./my-group/my-group.component";

export const MyGroupRoutes: Route[] = [
  {
    path: 'my-group', component: MyGroupComponent, canActivate: [GuardService]
  }
];
