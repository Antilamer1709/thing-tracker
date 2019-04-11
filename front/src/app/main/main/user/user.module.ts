import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddToGroupComponent } from './add-to-group/add-to-group.component';
import {AppRoutingModule} from "../../../app-routing.module";
import {ButtonModule} from "primeng/button";
import {AutoCompleteModule} from "primeng/primeng";
import {FormsModule} from "@angular/forms";
import {CommonSharedModule} from "../../../common/common-shared.module";

@NgModule({
  declarations: [AddToGroupComponent],
  imports: [
    AppRoutingModule,
    CommonModule,
    FormsModule,
    ButtonModule,
    AutoCompleteModule,
    CommonSharedModule
  ]
})
export class UserModule { }
