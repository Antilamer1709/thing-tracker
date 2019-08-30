import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {GroupService} from "../group.service";
import {GroupDTO} from "../../../../../generated/dto";
import {AppService} from "../../../../app.service";
import {NavigationExtras, Router} from "@angular/router";

@Component({
  selector: 'app-my-groups',
  templateUrl: './my-groups.component.html',
  styleUrls: ['./my-groups.component.scss']
})
export class MyGroupsComponent implements OnInit {

  public groupDTO: GroupDTO;
  public myGroups: GroupDTO[];

  constructor(private messageService: MessageService,
              private appService: AppService,
              private router: Router,
              private service: GroupService) { }

  ngOnInit() {
    Promise.resolve().then(() => {
      this.searchUserGroups();
    });
    this.groupDTO = new GroupDTO();
  }

  public createGroup(form: FormGroup): void {
    if (form.valid) {
      this.appService.blockedUI = true;
      this.service.saveGroup(this.groupDTO).subscribe(
        (res) => {
          console.log(res);
          this.messageService.add({severity:'success', summary:'Success', detail:'The group has been saved!'});
          this.searchUserGroups();
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

  public searchUserGroups(): void {
    this.appService.blockedUI = true;
    this.service.searchUserGroups().subscribe(
      (res) => {
        console.log(res);
        this.myGroups = res;
        this.appService.blockedUI = false;
      }
    );
  }

  public selectGroup(groupDTO: GroupDTO): void {
    this.router.navigate(['/main/my-groups/' + groupDTO.id]);
  }

}
