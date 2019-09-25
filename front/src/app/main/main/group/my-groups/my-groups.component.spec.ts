import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyGroupsComponent } from './my-groups.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {AutoCompleteModule, CardModule, MessageService} from "primeng/primeng";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('MyGroupsComponent', () => {
  let component: MyGroupsComponent;
  let fixture: ComponentFixture<MyGroupsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,
        AutoCompleteModule,
        CardModule,

        HttpClientTestingModule
      ],
      declarations: [MyGroupsComponent],
      providers: [
        MessageService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyGroupsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
