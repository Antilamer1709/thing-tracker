import {Component, Input, OnInit} from '@angular/core';
import {UserDTO} from "../../../../../../generated/dto";

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.scss']
})
export class ProfileInfoComponent implements OnInit {

  @Input() public user: UserDTO;

  constructor() {
  }

  ngOnInit() {
  }

}
