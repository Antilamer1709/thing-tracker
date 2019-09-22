import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../authentication/authentication.service';
import {HostDTO, MessageDTO, ResponseToMessageDTO} from '../../../generated/dto';
import {AuthRepository} from '../../authentication/repository/auth.repository';
import {MessageService} from 'primeng/api';
import {MainMenuService} from './main-menu.service';
import {Router} from '@angular/router';
import {UserService} from '../main/user/user.service';
import {AppService} from '../../app.service';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  public menuActive: boolean;

  public activeMenuId: string;

  public version: any;

  public messages: MessageDTO[];

  private stompClient: any;

  userMessagesStyle: HTMLElement;

  constructor(public authenticationService: AuthenticationService,
              public appService: AppService,
              private router: Router,
              private service: MainMenuService,
              private userService: UserService,
              private authRepo: AuthRepository,
              private messageService: MessageService) { }

  ngOnInit() {
    this.initSyles();
    this.initUserMessages();
    this.initializeWebSocketConnection();
    this.getVersion();
  }

  private initSyles() {
    this.userMessagesStyle = document.createElement('style');
    this.userMessagesStyle.innerHTML = '.user-messages-container {  max-height: ' + window.innerHeight + 'px; }';
    document.body.appendChild(this.userMessagesStyle);
  }

  private initUserMessages(): void {
    this.authenticationService.getLoggedUser().subscribe(res => {

        if (res && res.id) {
          this.service.getUserMessages().subscribe(messages => {
              this.messages = messages;
          });
        }

    });
  }

  private initializeWebSocketConnection(): void {
    this.stompClient = this.service.connect();

    if (this.stompClient) {
      this.stompClient.connect({}, frame => {
        this.openGlobalSocket();
        this.openSocket();
      });
    }
  }

  private openGlobalSocket(): void {
    this.stompClient.subscribe('/topic/user-messages', (message) => {
      this.handleResult(message);
    });
  }

  private handleResult(message): void {
    if (message.body) {
      const messageResult: MessageDTO = JSON.parse(message.body);
      console.log(messageResult);
      this.messages.push(messageResult);
      this.messageService.add({severity: 'info', summary: 'Info', detail: 'New message received!'});
    }
  }

  private openSocket(): void {
    this.authenticationService.getLoggedUser().subscribe(
      res => {
        if (res && res.id) {
          this.stompClient.subscribe('/topic/' + res.id, (message) => {
            this.handleResult(message);
          });
        }
      }
    );
  }

  public respondToMessage(messageDTO: MessageDTO, response: boolean): void {
    messageDTO.loading = true;

    const responseDTO: ResponseToMessageDTO = new ResponseToMessageDTO();
    responseDTO.response = response;
    responseDTO.messageId = messageDTO.id;

    this.userService.respondToMessage(responseDTO).subscribe(() => {
      this.initUserMessages();
    });
  }

  onMenuButtonClick(event: Event) {
    this.menuActive = !this.menuActive;
    event.preventDefault();
  }

  public logout(): void {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  private getVersion(): void {
    this.authenticationService.getAppVersion().subscribe(
      res => {
        this.version = res;
      }
    );
  }

}
