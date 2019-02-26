import { Component, OnInit } from '@angular/core';
import {MessageDTO} from "./main-menu-model";

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  public menuActive: boolean;

  public activeMenuId: string;

  public messages: MessageDTO[];

  constructor() { }

  ngOnInit() {
    //todo implement messages
    this.messages = [];
    this.messages.push(new MessageDTO(0, false, false, "Text message"));
    this.messages.push(new MessageDTO(1000, true, false, "File message"));
    this.messages.push(new MessageDTO(0, false, false, "Second text message, now longer"));
  }

  onMenuButtonClick(event: Event) {
    this.menuActive = !this.menuActive;
    event.preventDefault();
  }

}
