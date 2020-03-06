import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProfileInfoComponent} from './profile-info.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {MessageService, TabViewModule} from "primeng";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ProfileInfoComponent', () => {
  let component: ProfileInfoComponent;
  let fixture: ComponentFixture<ProfileInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,
        TabViewModule,

        HttpClientTestingModule
      ],
      declarations: [ProfileInfoComponent],
      providers: [
        MessageService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
