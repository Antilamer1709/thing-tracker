import {Component, Input, OnInit} from '@angular/core';
import {AppService} from "../../../../../app.service";
import {ProfileService} from "../../profile.service";
import {ExpenseDTO, ExpenseSearchDTO, ResponseDTO, SearchDTO, UserDTO} from "../../../../../../generated/dto";
import {ConfirmationService, LazyLoadEvent} from "primeng/api";
import {ExpensesService} from "../../../expenses/expenses.service";
import {AuthenticationService} from "../../../../../authentication/authentication.service";
import {Roles} from "../../../../../common/enums/RoleEnum";
import {ExpenseTypeComponent} from "../../../../../common/expense-type.component";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-profile-expenses',
  templateUrl: './profile-expenses.component.html',
  styleUrls: ['./profile-expenses.component.scss'],
  providers: [ConfirmationService]
})
export class ProfileExpensesComponent extends ExpenseTypeComponent implements OnInit {


  @Input() public user: UserDTO;
  public searchDTO: SearchDTO<ExpenseSearchDTO>;

  public cols: any[];
  public loading: boolean = false;
  public result: ResponseDTO<ExpenseDTO[]>;

  public showDeleteButton: boolean = false;

  constructor(private confirmationService: ConfirmationService,
              private appService: AppService,
              private authenticationService: AuthenticationService,
              private expensesService: ExpensesService,
              private profileService: ProfileService,
              protected service: ExpensesService) {
    super(service);
  }

  ngOnInit() {
    this.initShowDeleteButton();
    this.createSearchDTO();
  }

  public loadExpensesLazy(event: LazyLoadEvent) {
    Promise.resolve().then(() => {
      this.loading = true;
    });
    this.initSearchDTO(this.searchDTO, event);

    this.profileService.searchProfileExpenses(this.searchDTO).subscribe(
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

  private createSearchDTO(): void {
    this.searchDTO = new SearchDTO<ExpenseSearchDTO>();
    this.searchDTO.filter = new ExpenseSearchDTO();
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

  public initShowDeleteButton(): void {
    this.authenticationService.getLoggedUser().subscribe(loggedUser => {
      if (loggedUser.id === this.user.id) {
        this.showDeleteButton = true;
      } else if (this.authenticationService.hasRole(Roles.ADMIN)) {
        this.showDeleteButton = true;
      }
    });
  }

  public searchProfileExpenses(form: NgForm): void {
    this.loadExpensesLazy(null);
  }

}
