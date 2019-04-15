import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AppRoutingModule} from "../../../app-routing.module";
import {ButtonModule} from "primeng/button";
import {AutoCompleteModule} from "primeng/primeng";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CommonSharedModule} from "../../../common/common-shared.module";
import { MessagesComponent } from './messages/messages.component';

@NgModule({
  declarations: [MessagesComponent],
  imports: [
    AppRoutingModule,
    CommonModule,
    FormsModule,
    ButtonModule,
    AutoCompleteModule,
    CommonSharedModule,
    ReactiveFormsModule
  ]
})
export class UserModule { }
