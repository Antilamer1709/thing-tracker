import {Route} from "@angular/router";
import {GuardService} from "../../../common/guard-service";
import {MyGroupsComponent} from "./my-groups/my-groups.component";
import {GroupComponent} from "./group/group.component";

export const GroupRoutes: Route[] = [
  {
    path: 'my-groups', component: MyGroupsComponent, canActivate: [GuardService]
  },
  {
    path : 'my-groups/:id', component : GroupComponent
  }
];
