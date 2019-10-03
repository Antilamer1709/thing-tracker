import {Component, Input, OnInit} from '@angular/core';
import {AppService} from "../../../../../app.service";
import {ProfileService} from "../../profile.service";
import {
  ExpenseDTO,
  ExpenseSearchDTO,
  GroupmateType,
  ResponseDTO,
  SearchDTO,
  SelectGroupmateDTO,
  UserDTO
} from "../../../../../../generated/dto";
import {LazyLoadEvent} from "primeng/api";

@Component({
  selector: 'app-profile-expenses',
  templateUrl: './profile-expenses.component.html',
  styleUrls: ['./profile-expenses.component.scss']
})
export class ProfileExpensesComponent implements OnInit {


  @Input() public user: UserDTO;
  public searchDTO: SearchDTO<ExpenseSearchDTO>;

  public cols: any[];
  public loading: boolean = false;
  public result: ResponseDTO<ExpenseDTO[]>;

  constructor(private appService: AppService,
              private service: ProfileService) {
  }

  ngOnInit() {
    this.initSearchDTO();
  }

  private initSearchDTO(): void {
    this.searchDTO = new SearchDTO<ExpenseSearchDTO>();
    this.searchDTO.filter = new ExpenseSearchDTO();
    this.searchDTO.filter.selectGroupmateIds = [];

    this.searchDTO.filter.selectGroupmateIds.push(this.user.id);
  }


  public loadExpensesLazy(event: LazyLoadEvent) {
    this.loading = true;
    this.searchDTO.rows = event.rows;
    this.searchDTO.first = event.first;

    this.service.searchProfileExpenses(this.searchDTO).subscribe(
      (res) => {
        console.log(res);
        this.result = res;
        this.loading = false;
      }
    );
  }

}
