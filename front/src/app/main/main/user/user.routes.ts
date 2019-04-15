import {Route} from "@angular/router";
import {GuardService} from "../../../common/guard-service";
import {MessagesComponent} from "./messages/messages.component";

export const UserRoutes: Route[] = [
  {
    path: 'messages', component: MessagesComponent, canActivate: [GuardService]
  }
];
