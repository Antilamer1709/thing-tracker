import { NgModule } from '@angular/core';
import { MainComponent } from './main/main.component';
import {ButtonModule} from "primeng/primeng";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MainMenuComponent} from "./main-menu/main-menu.component";
import {CommonModule} from "@angular/common";
import {AppRoutingModule} from "../app-routing.module";
import {ExpensesModule} from "./expenses/expenses.module";
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  declarations: [MainComponent, MainMenuComponent, DashboardComponent],
  imports: [
    AppRoutingModule,
    CommonModule,
    BrowserAnimationsModule,
    ButtonModule,
    ExpensesModule
  ]
})
export class MainModule { }
