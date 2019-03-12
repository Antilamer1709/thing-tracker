import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyGroupComponent } from './my-group/my-group.component';
import { AddToGroupComponent } from './add-to-group/add-to-group.component';
import {AppRoutingModule} from "../../../app-routing.module";
import {ButtonModule} from "primeng/button";

@NgModule({
  declarations: [MyGroupComponent, AddToGroupComponent],
  imports: [
    AppRoutingModule,
    CommonModule,
    ButtonModule
  ]
})
export class UserModule { }
