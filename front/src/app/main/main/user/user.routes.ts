import {Route} from "@angular/router";
import {GuardService} from "../../../common/guard-service";
import {MyGroupComponent} from "./my-group/my-group.component";
import {AddToGroupComponent} from "./add-to-group/add-to-group.component";

export const MyGroupRoutes: Route[] = [
  {
    path: 'my-group', component: MyGroupComponent, canActivate: [GuardService]
  },
  {
    path: 'add-to-group', component: AddToGroupComponent, canActivate: [GuardService]
  }
];
