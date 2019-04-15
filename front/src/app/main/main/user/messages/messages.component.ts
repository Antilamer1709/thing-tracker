import { Component, OnInit } from '@angular/core';
import { Stomp} from 'stompjs/lib/stomp.js';
import SockJS from 'sockjs-client';
import {environment} from "../../../../../environments/environment";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessageService} from "primeng/api";
import {UserMessageService} from "./user-message.service";
import {AuthRepository} from "../../../../authentication/repository/auth.repository";
import {MessageDTO} from "../../../../../generated/dto";
import {AppService} from "../../../../app.service";
import {AuthenticationService} from "../../../../authentication/authentication.service";

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit {

  private serverUrl = environment.apiUrl + '/socket';
  isLoaded: boolean = false;
  isCustomSocketOpened = false;
  private stompClient;
  private form: FormGroup;
  private userForm: FormGroup;
  messages: MessageDTO[] = [];

  constructor(private socketService: UserMessageService,
              private authRepo: AuthRepository,
              private authenticationService: AuthenticationService,
              private messageService: MessageService
  ) { }

  ngOnInit() {
    this.form = new FormGroup({
      message: new FormControl(null, [Validators.required])
    });
    this.userForm = new FormGroup({
      fromId: new FormControl(null, [Validators.required]),
      toId: new FormControl(null)
    });
    this.initializeWebSocketConnection();
  }

  sendMessageUsingSocket() {
    if (this.form.valid) {
      let message: MessageDTO = { message: this.form.value.message, fromId: this.userForm.value.fromId, toId: this.userForm.value.toId };
      this.stompClient.send("/socket-subscriber/send/message", {}, JSON.stringify(message));
    }
  }

  sendMessageUsingRest() {
    if (this.form.valid) {
      let message: MessageDTO = { message: this.form.value.message, fromId: this.userForm.value.fromId, toId: this.userForm.value.toId };
      this.socketService.post(message).subscribe(res => {
        console.log(res);
      })
    }
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl + '?jwt=' + this.authRepo.getJWT().accessToken);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function (frame) {
      that.isLoaded = true;
      that.openGlobalSocket()
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/socket-publisher", (message) => {
      this.handleResult(message);
    });
  }

  openSocket() {
    if (this.isLoaded) {
      this.isCustomSocketOpened = true;
      this.stompClient.subscribe("/socket-publisher/"+this.userForm.value.fromId, (message) => {
        this.handleResult(message);
      });
    }
  }

  handleResult(message){
    if (message.body) {
      let messageResult: MessageDTO = JSON.parse(message.body);
      console.log(messageResult);
      this.messages.push(messageResult);
      this.messageService.add({severity:'success', summary:'Success', detail:'New message recieved!'});
    }
  }

}
