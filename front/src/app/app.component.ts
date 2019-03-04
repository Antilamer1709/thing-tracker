import {Component, OnInit} from '@angular/core';
import {Message} from "primeng/api";
import {Router} from "@angular/router";
import {AuthenticationService} from "./authentication/authentication.service";
import {MessageService} from "primeng/components/common/messageservice";
import {AppService} from "./app.service";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public msgs: Message[] = [];

  constructor(public authenticationService: AuthenticationService,
              public appService: AppService,
              private messageService: MessageService,
              private titleService: Title,
              private router: Router) {
    this.titleService.setTitle("Thing tracker");
  }

  ngOnInit(): void {
    this.initLoggedUser();
  }

  private initLoggedUser(): void {
    this.authenticationService.getLoggedUser().subscribe(
      res => {
        console.log("loggedUser: ");
        console.log(res);
        this.authenticationService.loggedUser = res;
      }
    );
  }

  public logout(): void {
    this.authenticationService.logout().subscribe(
      () => {
        this.authenticationService.loggedUser = null;
        this.messageService.add({severity:'info', summary:'Hello', detail:'You are logged out!'});
        this.router.navigate(['/']);
      }
    );
  }

}
