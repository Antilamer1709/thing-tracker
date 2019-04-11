import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import {TableModule} from "primeng/table";

@NgModule({
  declarations: [HeaderComponent],
  imports: [
    CommonModule,
    TableModule
  ],
  exports: [
    HeaderComponent
  ]
})
export class CommonSharedModule { }
