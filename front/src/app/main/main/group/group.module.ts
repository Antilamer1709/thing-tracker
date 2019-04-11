import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyGroupsComponent } from './my-groups/my-groups.component';
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {CardModule, InputTextModule} from "primeng/primeng";
import {CommonSharedModule} from "../../../common/common-shared.module";
import { GroupComponent } from './group/group.component';

@NgModule({
  declarations: [MyGroupsComponent, GroupComponent],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    CardModule,
    CommonSharedModule
  ]
})
export class GroupModule { }
