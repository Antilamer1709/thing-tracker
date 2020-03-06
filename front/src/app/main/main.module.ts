import { NgModule } from '@angular/core';
import { MainComponent } from './main/main.component';
import {ButtonModule, CardModule, OverlayPanelModule, ProgressBarModule} from "primeng";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MainMenuComponent} from "./main-menu/main-menu.component";
import {CommonModule} from "@angular/common";
import {AppRoutingModule} from "../app-routing.module";
import {ExpensesModule} from "./main/expenses/expenses.module";
import { DashboardComponent } from './main/dashboard/dashboard.component';
import {UserModule} from "./main/user/user.module";
import {GroupModule} from "./main/group/group.module";
import {CommonSharedModule} from "../common/common-shared.module";
import {ProfileModule} from "./main/profile/profile.module";

@NgModule({
  declarations: [MainComponent, MainMenuComponent, DashboardComponent],
  imports: [
    AppRoutingModule,
    CommonModule,
    BrowserAnimationsModule,
    ButtonModule,
    ExpensesModule,
    UserModule,
    GroupModule,
    CommonSharedModule,
    OverlayPanelModule,
    CardModule,
    ProgressBarModule,
    ProfileModule
  ]
})
export class MainModule { }
