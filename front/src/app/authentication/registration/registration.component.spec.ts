import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationComponent } from './registration.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MessageService} from "primeng/api";
import {LoginService} from "../login/login.service";
import {RegistrationService} from "./registration.service";

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,

        HttpClientTestingModule
      ],
      declarations: [RegistrationComponent],
      providers: [
        MessageService,
        LoginService,
        RegistrationService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
