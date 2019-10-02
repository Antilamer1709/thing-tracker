import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile/profile.component';
import {InputTextModule, TabViewModule} from "primeng/primeng";
import { ProfileInfoComponent } from './profile/profile-info/profile-info.component';
import {CommonSharedModule} from "../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import { ProfileExpensesComponent } from './profile/profile-expenses/profile-expenses.component';
import {TableModule} from "primeng/table";

@NgModule({
  declarations: [ProfileComponent, ProfileInfoComponent, ProfileExpensesComponent],
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    TabViewModule,
    CommonSharedModule,
    TableModule
  ]
})
export class ProfileModule { }
