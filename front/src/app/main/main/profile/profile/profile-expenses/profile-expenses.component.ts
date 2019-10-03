import {Component, Input, OnInit} from '@angular/core';
import {AppService} from "../../../../../app.service";
import {ProfileService} from "../../profile.service";
import {ExpenseDTO, ExpenseSearchDTO, ResponseDTO, SearchDTO, UserDTO} from "../../../../../../generated/dto";
import {ConfirmationService, LazyLoadEvent} from "primeng/api";
import {ExpensesService} from "../../../expenses/expenses.service";

@Component({
  selector: 'app-profile-expenses',
  templateUrl: './profile-expenses.component.html',
  styleUrls: ['./profile-expenses.component.scss'],
  providers: [ConfirmationService]
})
export class ProfileExpensesComponent implements OnInit {


  @Input() public user: UserDTO;
  public searchDTO: SearchDTO<ExpenseSearchDTO>;

  public cols: any[];
  public loading: boolean = false;
  public result: ResponseDTO<ExpenseDTO[]>;

  constructor(private confirmationService: ConfirmationService,
              private appService: AppService,
              private expensesService: ExpensesService,
              private service: ProfileService) {
  }

  ngOnInit() {
    this.initSearchDTO();
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

  public showDeleteRow(row: ExpenseDTO): void {
    this.confirmationService.confirm({
      header: 'Are you sure?',
      message: 'Delete expense with ID: ' + row.id + ', types : ' + row.types,
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => this.onDeleteRow(row)
    });
  }

  private initSearchDTO(): void {
    this.searchDTO = new SearchDTO<ExpenseSearchDTO>();
    this.searchDTO.filter = new ExpenseSearchDTO();
    this.searchDTO.filter.selectGroupmateIds = [];

    this.searchDTO.filter.selectGroupmateIds.push(this.user.id);
  }

  private onDeleteRow(row: ExpenseDTO): void {
    this.appService.blockedUI = true;

    this.expensesService.deleteExpense(row.id).subscribe(
      (res) => {
        console.log(res);
        this.appService.blockedUI = false;
      }
    );
  }

}
