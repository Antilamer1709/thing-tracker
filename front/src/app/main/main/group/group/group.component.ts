import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {GroupDTO, UserDTO} from "../../../../../generated/dto";
import {GroupService} from "../group.service";
import {AppService} from "../../../../app.service";
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {UserService} from "../../user/user.service";

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

}
