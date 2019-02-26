import { NgModule } from '@angular/core';
import { MainComponent } from './main/main.component';
import {ButtonModule, OverlayPanelModule} from "primeng/primeng";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MainMenuComponent} from "./main-menu/main-menu.component";
import {CommonModule} from "@angular/common";
import {AppRoutingModule} from "../app-routing.module";

@NgModule({
  declarations: [MainComponent, MainMenuComponent],
  imports: [
    AppRoutingModule,
    CommonModule,
    OverlayPanelModule,
    BrowserAnimationsModule,
    ButtonModule
  ]
})
export class MainModule { }
