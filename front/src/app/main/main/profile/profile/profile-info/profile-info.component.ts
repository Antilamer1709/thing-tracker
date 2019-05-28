import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {AppService} from "../../../../../app.service";
import {ProfileService} from "../../profile.service";
import {UserDTO} from "../../../../../../generated/dto";
import {AuthenticationService} from "../../../../../authentication/authentication.service";

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.scss']
})
export class ProfileInfoComponent implements OnInit {

  public user: UserDTO;
  private userId: number;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private appService: AppService,
              private authenticationService: AuthenticationService,
              private service: ProfileService) { }

  ngOnInit() {
    this.initProfileInfo();
  }

  private initProfileInfo(): void {
    this.appService.blockedUI = true;

    this.route.params.subscribe((params: Params) => {

      this.userId = params['id'];
      this.fillProfileData();

    });
  }

  private fillProfileData(): void {
    if (this.userId) {
      this.appService.blockedUI = true;

      this.service.getUser(this.userId).subscribe(
        (res) => {
          console.log(res);
          this.user = res;
          this.appService.blockedUI = false;
        }, err => {
          this.router.navigate(['/main/my-groups']);
        }
      );
    } else {
      this.user = this.authenticationService.loggedUser;
      this.appService.blockedUI = false;
    }
  }

}
