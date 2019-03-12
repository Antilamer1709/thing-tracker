import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormGroup} from "@angular/forms";
import {LoginService} from "./login.service";
import {MessageService} from "primeng/components/common/messageservice";
import {AuthenticationService} from "../authentication.service";
import {JwtAuthenticationResponseDTO, UserDTO} from "../../../generated/dto";
import {AppService} from "../../app.service";
import {AuthRepository} from "../repository/auth.repository";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public user: UserDTO;
  private returnUrl: string;
  private token: string;

  constructor(private messageService: MessageService,
              private authenticationService: AuthenticationService,
              private service: LoginService,
              private appService: AppService,
              private router: Router,
              private authRepo: AuthRepository,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.user = new UserDTO();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.token = this.route.snapshot.queryParams['token'];
    this.checkOauth2();
  }

  public login(form: FormGroup): void {
    if (form.valid) {
      this.appService.blockedUI = true;
      this.service.login(this.user).subscribe(
        (res) => {
          console.log(res);
          this.authenticationService.initLoggedUser();
          this.appService.blockedUI = false;

          this.successNavigate();
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

  private successNavigate(): void {
    this.messageService.add({severity:'success', summary:'Hello', detail:'You are logged in!'});
    this.router.navigate([this.returnUrl]);
  }

  public registration(): void {
    this.router.navigate(['registration']);
  }

  private checkOauth2(): void {
    if (this.token) {
      let token: JwtAuthenticationResponseDTO = new JwtAuthenticationResponseDTO();
      token.accessToken = this.token;
      token.tokenType = 'Bearer';
      this.authRepo.auth("facebook", token);
      this.successNavigate();
      this.authenticationService.initLoggedUser();
    }
  }

}
