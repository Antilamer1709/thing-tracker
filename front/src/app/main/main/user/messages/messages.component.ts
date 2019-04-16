import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessageService} from "primeng/api";
import {UserMessageService} from "./user-message.service";
import {AuthRepository} from "../../../../authentication/repository/auth.repository";
import {MessageDTO} from "../../../../../generated/dto";
import {AuthenticationService} from "../../../../authentication/authentication.service";

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit {

  public messages: MessageDTO[] = [];
  private isLoaded: boolean = false;
  private isCustomSocketOpened = false;
  private form: FormGroup;
  private userForm: FormGroup;
  private stompClient;

  constructor(private userMessageService: UserMessageService,
              private authRepo: AuthRepository,
              private authenticationService: AuthenticationService,
              private messageService: MessageService
  ) {
  }

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
      let message: MessageDTO = {
        message: this.form.value.message,
        fromId: this.userForm.value.fromId,
        toId: this.userForm.value.toId
      };
      this.stompClient.send("/socket-subscriber/send/message", {}, JSON.stringify(message));
    }
  }

  sendMessageUsingRest() {
    if (this.form.valid) {
      let message: MessageDTO = {
        message: this.form.value.message,
        fromId: this.userForm.value.fromId,
        toId: this.userForm.value.toId
      };
      this.userMessageService.post(message).subscribe(res => {
        console.log(res);
      })
    }
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/socket-publisher", (message) => {
      this.handleResult(message);
    });
  }

  openSocket() {
    if (this.isLoaded) {
      this.isCustomSocketOpened = true;
      this.stompClient.subscribe("/socket-publisher/" + this.userForm.value.fromId, (message) => {
        this.handleResult(message);
      });
    }
  }

  handleResult(message) {
    if (message.body) {
      let messageResult: MessageDTO = JSON.parse(message.body);
      console.log(messageResult);
      this.messages.push(messageResult);
      this.messageService.add({severity: 'success', summary: 'Success', detail: 'New message recieved!'});
    }
  }

  private initializeWebSocketConnection(): void {
    this.stompClient = this.userMessageService.connect();

    this.stompClient.connect({}, frame => {
      this.isLoaded = true;
      this.openGlobalSocket();
    });
  }

}
