import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MainMenuComponent } from './main-menu.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CommonSharedModule} from "../../common/common-shared.module";
import {FormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {CardModule, MessageService, OverlayPanelModule, ProgressBarModule} from "primeng";

describe('MainMenuComponent', () => {
  let component: MainMenuComponent;
  let fixture: ComponentFixture<MainMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CommonSharedModule,
        FormsModule,
        OverlayPanelModule,
        CardModule,
        ProgressBarModule,

        HttpClientTestingModule
      ],
      declarations: [ MainMenuComponent ],
      providers: [
        MessageService,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
