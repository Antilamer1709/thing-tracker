import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {AuthenticationModule} from "./authentication/authentication.module";
import {RouterModule} from "@angular/router";
import {routes} from "./app.routes";
import {ConfirmationService, MessageService} from "primeng/api";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {CustomHttpInterceptor} from "./common/http-interceptor";
import {GrowlModule} from "primeng/growl";
import {BlockUIModule} from "primeng/blockui";
import {ButtonModule} from "primeng/button";
import {ConfirmDialogModule} from "primeng/primeng";
import {MainModule} from "./main/main.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot( routes, { useHash: true } ),
    AuthenticationModule,
    GrowlModule,
    BlockUIModule,
    ButtonModule,
    ConfirmDialogModule,
    MainModule
  ],
  providers: [
    MessageService,
    ConfirmationService,
    {provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
