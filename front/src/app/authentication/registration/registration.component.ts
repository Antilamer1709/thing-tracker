import { Component, OnInit } from '@angular/core';
import {RegistrationService} from "./registration.service";
import {MessageService} from "primeng/components/common/messageservice";
import {Router} from "@angular/router";
import {FormGroup} from "@angular/forms";
import {RegistrationDTO} from "../../../generated/dto";
import {AppService} from "../../app.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  public registration: RegistrationDTO;

  constructor(private service: RegistrationService,
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
          this.appService.blockedUI = false;
          this.messageService.add({severity:'success', summary:'Registration', detail:'You can log in now!'});
          this.router.navigate(['/authentication/login']);
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

}
