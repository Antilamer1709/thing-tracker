import { Component, OnInit } from '@angular/core';
import {UserDTO} from "../../../../../generated/dto";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {AppService} from "../../../../app.service";
import {AuthenticationService} from "../../../../authentication/authentication.service";
import {ProfileService} from "../profile.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

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
