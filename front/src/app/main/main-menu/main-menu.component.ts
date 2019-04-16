import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";
import {MessageDTO} from "../../../generated/dto";

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  public menuActive: boolean;

  public activeMenuId: string;

  public messages: MessageDTO[];

  constructor(public authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.messages = [];
  }

  onMenuButtonClick(event: Event) {
    this.menuActive = !this.menuActive;
    event.preventDefault();
  }

}
