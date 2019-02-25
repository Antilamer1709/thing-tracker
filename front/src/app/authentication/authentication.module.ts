import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import {LoginService} from "./login/login.service";
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/primeng";
import {AngularFontAwesomeModule} from "angular-font-awesome";
import { RegistrationComponent } from './registration/registration.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    AngularFontAwesomeModule
  ],
  declarations: [LoginComponent, RegistrationComponent],
  providers: [LoginService]
})
export class AuthenticationModule { }
