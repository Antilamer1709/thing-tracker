import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyGroupsComponent } from './my-groups/my-groups.component';
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/primeng";

@NgModule({
  declarations: [MyGroupsComponent],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule
  ]
})
export class GroupModule { }
