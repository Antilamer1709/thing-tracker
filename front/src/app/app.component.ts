import {Component, OnInit} from '@angular/core';
import {Message} from "primeng/api";
import {Router} from "@angular/router";
import {AuthenticationService} from "./authentication/authentication.service";
import {MessageService} from "primeng/components/common/messageservice";
import {AppService} from "./app.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(public authenticationService: AuthenticationService,
              public appService: AppService,
              private messageService: MessageService,
              private router: Router) {
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
        this.messageService.add({severity:'success', summary:'Hello', detail:'You are logged out!'});
        this.router.navigate(['/']);
      }
    );
  }

}
