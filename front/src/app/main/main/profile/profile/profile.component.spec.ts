import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProfileComponent} from './profile.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {MessageService, TabViewModule} from "primeng/primeng";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ProfileInfoComponent} from "./profile-info/profile-info.component";

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,
        TabViewModule,

        HttpClientTestingModule
      ],
      declarations: [ProfileComponent, ProfileInfoComponent],
      providers: [
        MessageService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
