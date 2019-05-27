import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import {TableModule} from "primeng/table";
import { RolesPipe } from './pipes/roles.pipe';

@NgModule({
  declarations: [HeaderComponent, RolesPipe],
  imports: [
    CommonModule,
    TableModule
  ],
  exports: [
    HeaderComponent,
    RolesPipe
  ]
})
export class CommonSharedModule { }
