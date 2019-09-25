import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import {ToastModule} from "primeng/toast";
import {BlockUIModule} from "primeng/blockui";
import {ButtonModule} from "primeng/button";
import {ConfirmationService, ConfirmDialogModule, MessageService, ProgressSpinnerModule} from "primeng/primeng";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GuardService} from "./common/guard-service";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {JwtInterceptor} from "./common/jwt.interceptor";
import {CustomHttpInterceptor} from "./common/http-interceptor";
import {UpdateDateHttpInterceptor} from "./common/update-date-http-interceptor";

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        ToastModule,
        BlockUIModule,
        ButtonModule,
        ConfirmDialogModule,
        ProgressSpinnerModule,

        HttpClientTestingModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        MessageService,
        ConfirmationService,
        GuardService,
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: UpdateDateHttpInterceptor, multi: true}],
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

});
