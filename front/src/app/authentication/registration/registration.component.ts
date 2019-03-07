import { Component, OnInit } from '@angular/core';
import {RegistrationService} from "./registration.service";
import {MessageService} from "primeng/components/common/messageservice";
import {Router} from "@angular/router";
import {FormGroup} from "@angular/forms";
import {RegistrationDTO, UserDTO} from "../../../generated/dto";
import {AppService} from "../../app.service";
import {LoginService} from "../login/login.service";
import {AuthenticationService} from "../authentication.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  public registration: RegistrationDTO;

  constructor(private service: RegistrationService,
              private loginService: LoginService,
              private authenticationService: AuthenticationService,
              private router: Router,
              private appService: AppService,
              private messageService: MessageService) {
  }

  ngOnInit() {
    this.registration = new RegistrationDTO();
  }

  public register(form: FormGroup): void {
    console.log(form);
    if (form.valid && this.registration.password === this.registration.confirmPassword) {
      this.appService.blockedUI = true;
      this.service.register(this.registration).subscribe(
        () => {
          this.messageService.add({severity:'success', summary:'Registration', detail:'Registration has been successfully completed!'});
          this.login();
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

  public login(): void {
    let user: UserDTO = new UserDTO();
    user.username = this.registration.username;
    user.password = this.registration.password;

    this.loginService.authenticate(user).subscribe(
      (res) => {
        console.log(res);
        this.appService.blockedUI = false;

        this.messageService.add({severity:'info', summary:'Hello', detail:'You are logged in!'});
        this.authenticationService.loggedUser = res;
        this.router.navigate(['/']);
      }
    );
  }

  public loginPage(): void {
    this.router.navigate(['login']);
  }

}
