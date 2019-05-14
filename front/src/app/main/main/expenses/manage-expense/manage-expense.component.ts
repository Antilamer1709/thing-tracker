import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {AppService} from "../../../../app.service";
import {ExpensesService} from "../expenses.service";
import {ExpenseSearchChartDTO, ExpenseSearchDTO, SelectGroupmateDTO} from "../../../../../generated/dto";
import {CommonComponent} from "../../../../common/common-component";
import {GroupService} from "../../group/group.service";

@Component({
  selector: 'app-manage-expense',
  templateUrl: './manage-expense.component.html',
  styleUrls: ['./manage-expense.component.scss'],
  styles: [`
        :host ::ng-deep .ui-multiselected-item-token,
        :host ::ng-deep .ui-multiselected-empty-token {
            padding: 2px 8px;
            margin: 0 0.27em 0 0;
            display: inline-block;
            vertical-align:middle;
        }

        :host ::ng-deep .ui-multiselected-item-token {
            background: #404C51;
            color: #ffffff;
        }
    `]
})
export class ManageExpenseComponent extends CommonComponent implements OnInit {

  public expenseSearchDTO: ExpenseSearchDTO;
  public groupmatesOptions: SelectGroupmateDTO[];
  public chartData: any;

  constructor(private messageService: MessageService,
              private appService: AppService,
              private groupService: GroupService,
              private service: ExpensesService) {
    super();
  }

  ngOnInit() {
    this.expenseSearchDTO = new ExpenseSearchDTO();
    this.expenseSearchDTO.selectGroupmates = [];
    this.expenseSearchDTO.dateTo = new Date();
    this.getGroupmates();
  }

  public searchExpenses(form: FormGroup): void {
    if (form.valid && this.expenseSearchDTO.dateTo >= this.expenseSearchDTO.dateFrom) {
      this.appService.blockedUI = true;
      this.service.searchChartExpenses(this.expenseSearchDTO).subscribe(
        (res) => {
          console.log(res);
          this.appService.blockedUI = false;
          this.initChartData(res);
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

  public getGroupmates(): void {
    this.appService.blockedUI = true;
    this.groupService.getGroupmates().subscribe(
      (res) => {
        this.groupmatesOptions = res;
        this.appService.blockedUI = false;
      }
    );
  }

  private initChartData(res: ExpenseSearchChartDTO) {
    this.chartData = {
      labels: Object.keys(res.data),
      datasets: [
        {
          data: Object.values(res.data),
          backgroundColor: [
            "#FF6384",
            "#36A2EB",
            "#FFCE56",
            "#56ff89",
            "#c456ff",
            "#56fff9",
            "#ff9f56",
            "#ff56aa",
            "#ebff56",
            "#56c6ff"
          ],
          hoverBackgroundColor: [
            "#FF6384",
            "#36A2EB",
            "#FFCE56",
            "#56ff89",
            "#c456ff",
            "#56fff9",
            "#ff9f56",
            "#ff56aa",
            "#ebff56",
            "#56c6ff"
          ]
        }]
    };
  }
}
