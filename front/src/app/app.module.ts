import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {AuthenticationModule} from "./authentication/authentication.module";
import {RouterModule} from "@angular/router";
import {routes} from "./app.routes";
import {ConfirmationService, MessageService} from "primeng/api";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {CustomHttpInterceptor} from "./common/http-interceptor";
import {BlockUIModule} from "primeng/blockui";
import {ButtonModule} from "primeng/button";
import {ConfirmDialogModule, ProgressSpinnerModule} from "primeng/primeng";
import {MainModule} from "./main/main.module";
import {ToastModule} from "primeng/toast";
import {UpdateDateHttpInterceptor} from "./common/update-date-http-interceptor";
import {GuardService} from "./common/guard-service";
import {JwtInterceptor} from "./common/jwt.interceptor";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot( routes, { useHash: true } ),
    AuthenticationModule,
    ToastModule,
    BlockUIModule,
    ButtonModule,
    ConfirmDialogModule,
    MainModule,
    ProgressSpinnerModule
  ],
  providers: [
    MessageService,
    ConfirmationService,
    GuardService,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: UpdateDateHttpInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
