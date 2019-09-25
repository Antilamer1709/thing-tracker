import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {CommonSharedModule} from "../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {MessageService} from "primeng/api";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {LoginService} from "./login.service";
import {RouterTestingModule} from "@angular/router/testing";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,

        HttpClientTestingModule
      ],
      declarations: [LoginComponent],
      providers: [
        MessageService,
        LoginService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
