import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import {LoginService} from "./login/login.service";
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng";
import { RegistrationComponent } from './registration/registration.component';
import {CommonSharedModule} from "../common/common-shared.module";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    CommonSharedModule
  ],
  declarations: [LoginComponent, RegistrationComponent],
  providers: [LoginService]
})
export class AuthenticationModule { }
