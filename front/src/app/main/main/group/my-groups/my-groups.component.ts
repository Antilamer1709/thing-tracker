import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {GroupService} from "../group.service";
import {GroupDTO} from "../../../../../generated/dto";

@Component({
  selector: 'app-my-groups',
  templateUrl: './my-groups.component.html',
  styleUrls: ['./my-groups.component.scss']
})
export class MyGroupsComponent implements OnInit {

  public groupDTO: GroupDTO;

  constructor(private messageService: MessageService,
              private service: GroupService) { }

  ngOnInit() {
    this.groupDTO = new GroupDTO();
  }

  public createGroup(form: FormGroup): void {
    if (form.valid) {
      this.service.saveGroup(this.groupDTO).subscribe(
        (res) => {
          console.log(res);
          this.messageService.add({severity:'success', summary:'Success', detail:'The group has been saved!'});
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

}
