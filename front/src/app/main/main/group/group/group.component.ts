import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {GroupDTO} from "../../../../../generated/dto";
import {GroupService} from "../group.service";
import {AppService} from "../../../../app.service";

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.scss']
})
export class GroupComponent implements OnInit {

  public groupId: number;
  public groupDTO: GroupDTO;

  constructor(private route: ActivatedRoute,
              private appService: AppService,
              private service: GroupService) { }

  ngOnInit() {
    this.initGroup();
  }

  private initGroup(): void {
    this.appService.blockedUI = false;

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

}
