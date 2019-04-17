import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "./authentication/authentication.service";
import {AppService} from "./app.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(public authenticationService: AuthenticationService,
              public appService: AppService) {
  }

  ngOnInit(): void {
    this.initLoggedUser();
  }

  private initLoggedUser(): void {
    this.authenticationService.initLoggedUser();
  }

}
