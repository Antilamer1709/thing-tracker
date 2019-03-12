import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {AppService} from "../../../app.service";
import {ExpensesService} from "../expenses.service";
import {ExpenseSearchChartDTO, ExpenseSearchDTO} from "../../../../generated/dto";
import {CommonComponent} from "../../../common/common-component";

@Component({
  selector: 'app-manage-expense',
  templateUrl: './manage-expense.component.html',
  styleUrls: ['./manage-expense.component.scss']
})
export class ManageExpenseComponent extends CommonComponent implements OnInit {

  public expenseSearchDTO: ExpenseSearchDTO;
  public chartData: any;

  constructor(private messageService: MessageService,
              private appService: AppService,
              private service: ExpensesService) {
    super();
  }

  ngOnInit() {
    this.expenseSearchDTO = new ExpenseSearchDTO();
    this.expenseSearchDTO.dateTo = new Date();
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
