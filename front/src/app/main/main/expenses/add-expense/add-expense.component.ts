import { Component, OnInit } from '@angular/core';
import {ExpensesService} from "../expenses.service";
import {FormGroup} from "@angular/forms";
import {ExpenseDTO} from "../../../../../generated/dto";
import {MessageService} from "primeng/api";
import {AppService} from "../../../../app.service";
import {ExpenseTypeComponent} from "../../../../common/expense-type.component";

@Component({
  selector: 'app-add-expense',
  templateUrl: './add-expense.component.html',
  styleUrls: ['./add-expense.component.scss']
})
export class AddExpenseComponent extends ExpenseTypeComponent implements OnInit {

  public expense: ExpenseDTO;

  constructor(private messageService: MessageService,
              private appService: AppService,
              protected service: ExpensesService) {
    super(service);
  }

  ngOnInit() {
    this.createNewExpense();
  }

  private createNewExpense(): void {
    this.expense = new ExpenseDTO();
    this.selectedExpenseTypes = [];
    this.expense.types = this.selectedExpenseTypes;
  }

  public saveExpense(form: FormGroup): void {
    if (form.valid) {
      this.appService.blockedUI = true;
      this.service.saveExpense(this.expense).subscribe(
        (res) => {
          console.log(res);
          this.appService.blockedUI = false;

          this.createNewExpense();
          this.messageService.add({severity:'success', summary:'Success', detail:'The expense has been saved!'});
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

}
