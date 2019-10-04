import { Component, OnInit } from '@angular/core';
import {UserDTO} from "../../../../../generated/dto";
import {AuthenticationService} from "../../../../authentication/authentication.service";
import {AppService} from "../../../../app.service";

@Component({
  selector: 'app-my-expenses',
  templateUrl: './my-expenses.component.html',
  styleUrls: ['./my-expenses.component.scss']
})
export class MyExpensesComponent implements OnInit {

  public loggedUser: UserDTO;

  constructor(private appService: AppService,
              private authenticationService: AuthenticationService) { }

  ngOnInit() {
    Promise.resolve().then(() => {
      this.initLoggedUser();
    });
  }

  private initLoggedUser(): void {
    this.appService.blockedUI = true;

    this.authenticationService.getLoggedUser().subscribe(loggedUser => {
      this.loggedUser = loggedUser;
      this.appService.blockedUI = false;
    });
  }
}
