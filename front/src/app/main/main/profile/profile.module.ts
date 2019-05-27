import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile/profile.component';
import {InputTextModule, TabViewModule} from "primeng/primeng";
import { ProfileInfoComponent } from './profile/profile-info/profile-info.component';
import {CommonSharedModule} from "../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [ProfileComponent, ProfileInfoComponent],
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    TabViewModule,
    CommonSharedModule
  ]
})
export class ProfileModule { }
