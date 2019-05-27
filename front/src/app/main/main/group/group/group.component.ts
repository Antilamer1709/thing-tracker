import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {GroupDTO, UserDTO} from "../../../../../generated/dto";
import {GroupService} from "../group.service";
import {AppService} from "../../../../app.service";
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {UserService} from "../../user/user.service";
import {AuthenticationService} from "../../../../authentication/authentication.service";
import {Roles} from "../../../../common/enums/RoleEnum";

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.scss']
})
export class GroupComponent implements OnInit {

  public groupId: number;
  public groupDTO: GroupDTO;

  public users: UserDTO[];
  public userSuggestions: UserDTO[];

  constructor(private route: ActivatedRoute,
              private appService: AppService,
              private messageService: MessageService,
              private userService: UserService,
              private router: Router,
              private authenticationService: AuthenticationService,
              private service: GroupService) { }

  ngOnInit() {
    this.users = [];
    this.initGroup();
  }

  private initGroup(): void {
    this.appService.blockedUI = true;

    this.route.queryParams.subscribe(params => {
      this.groupId = params["id"];
    });
    this.route.params.subscribe((params: Params) => {

      this.groupId = params['id'];
      this.service.getUserGroup(this.groupId).subscribe(
        (res) => {
          console.log(res);
          this.groupDTO = res;
          this.appService.blockedUI = false;
        }
      );

    });
  }


  public searchUserSuggestions(event): void {
    this.userService.searchUserSuggestions(event.query).subscribe(
      (res) => {
        console.log(res);
        this.userSuggestions = res;
      }
    );
  }

  public inviteToGroup(form: FormGroup): void {
    if (form.valid) {
      this.appService.blockedUI = true;

      this.service.inviteUsersToGroup(this.groupId, this.users).subscribe(
        (res) => {
          console.log(res);
          this.appService.blockedUI = false;
          this.messageService.add({severity:'success', summary:'Success', detail:'The user has been sent an invite!'});
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

  public showKickButton(user: UserDTO): boolean {
    if (user.id !== this.groupDTO.creator.id) {
      if (this.authenticationService.hasRole(Roles.ADMIN)) {
        return true;
      }
      if (this.groupDTO.creator.id === this.authenticationService.loggedUser.id) {
        return true;
      }
    }

    return false;
  }

  public kickUser(user: UserDTO): void {
    this.appService.blockedUI = true;

    this.service.kickUserFromGroup(this.groupId, user.id).subscribe(
      (res) => {
        this.messageService.add({severity:'success', summary:'Success', detail:'User has been kicked!'});
        this.initGroup();
      }
    );
  }

  public leaveGroup(): void {
    this.appService.blockedUI = true;

    this.service.kickUserFromGroup(this.groupId, this.authenticationService.loggedUser.id).subscribe(
      (res) => {
        this.messageService.add({severity:'success', summary:'Success', detail:'You have left the group!'});
        this.router.navigate(['/main/my-groups']);
      }
    );
  }

}
