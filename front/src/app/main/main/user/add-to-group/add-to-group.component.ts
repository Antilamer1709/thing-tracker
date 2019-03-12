import { Component, OnInit } from '@angular/core';
import {UserDTO} from "../../../../../generated/dto";
import {UserService} from "../user.service";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-add-to-group',
  templateUrl: './add-to-group.component.html',
  styleUrls: ['./add-to-group.component.scss']
})
export class AddToGroupComponent implements OnInit {

  public users: UserDTO[];
  public userSuggestions: UserDTO[];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.users = [];
  }

  public searchUserSuggestions(event): void {
    this.userService.searchUserSuggestions(event.query).subscribe(
      (res) => {
        console.log(res);
        this.userSuggestions = res;
      }
    );
  }

  public addToGroup(form: FormGroup): void {

  }

}
