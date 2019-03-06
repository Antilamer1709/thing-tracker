import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MessageService} from "primeng/api";
import {AppService} from "../../../app.service";
import {ExpensesService} from "../expenses.service";
import {ExpenseSearchDTO} from "../../../../generated/dto";

@Component({
  selector: 'app-manage-expense',
  templateUrl: './manage-expense.component.html',
  styleUrls: ['./manage-expense.component.scss']
})
export class ManageExpenseComponent implements OnInit {

  public expenseSearchDTO: ExpenseSearchDTO;

  constructor(private messageService: MessageService,
              private appService: AppService,
              private service: ExpensesService) { }

  ngOnInit() {
    this.expenseSearchDTO = new ExpenseSearchDTO();
    this.expenseSearchDTO.dateTo = new Date();
  }

  public searchExpenses(form: FormGroup): void {
    this.service.searchChartExpenses(this.expenseSearchDTO).subscribe(
      (res) => {
        console.log(res);
      }
    );
  }

}
