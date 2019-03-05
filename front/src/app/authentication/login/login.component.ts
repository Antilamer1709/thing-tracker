import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormGroup} from "@angular/forms";
import {LoginService} from "./login.service";
import {MessageService} from "primeng/components/common/messageservice";
import {AuthenticationService} from "../authentication.service";
import {UserDTO} from "../../../generated/dto";
import {AppService} from "../../app.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public user: UserDTO;
  private returnUrl: string;

  constructor(private messageService: MessageService,
              private authenticationService: AuthenticationService,
              private service: LoginService,
              private appService: AppService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.user = new UserDTO();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  public login(form: FormGroup): void {
    if (form.valid) {
      this.appService.blockedUI = true;
      this.service.authenticate(this.user).subscribe(
        (res) => {
          console.log(res);
          this.appService.blockedUI = false;

          this.messageService.add({severity:'success', summary:'Hello', detail:'You are logged in!'});
          this.authenticationService.loggedUser = res;
          this.router.navigate([this.returnUrl]);
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

  public registration(): void {
    this.router.navigate(['registration']);
  }

}
