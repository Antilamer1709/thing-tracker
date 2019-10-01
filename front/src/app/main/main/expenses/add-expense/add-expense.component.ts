import { Component, OnInit } from '@angular/core';
import {ExpensesService} from "../expenses.service";
import {FormGroup} from "@angular/forms";
import {ExpenseDTO} from "../../../../../generated/dto";
import {MessageService} from "primeng/api";
import {AppService} from "../../../../app.service";

@Component({
  selector: 'app-add-expense',
  templateUrl: './add-expense.component.html',
  styleUrls: ['./add-expense.component.scss']
})
export class AddExpenseComponent implements OnInit {

  public expense: ExpenseDTO;
  public expenseTypes: string[];

  constructor(private messageService: MessageService,
              private appService: AppService,
              private service: ExpensesService) { }

  ngOnInit() {
    this.createNewExpense();
    this.expenseTypes = [];
  }

  private createNewExpense(): void {
    this.expense = new ExpenseDTO();
    this.expense.types = [];
  }

  public searchExpenseTypes(event): void {
    this.service.searchExpenseTypes(event.query).subscribe(
      (res) => {
        console.log(res);
        this.expenseTypes = res;
      }
    );
  }

  public expenseTypesOnKeyUp(event: KeyboardEvent): void {
    if (event.key == "Enter") {
      this.addAutocompleteValue(event);
    }
  }

  public expenseTypesOnBlur(event: FocusEvent): void {
    this.addAutocompleteValue(event);
  }

  private addAutocompleteValue(event): void {
    let tokenInput = event.srcElement as any;
    if (tokenInput.value && tokenInput.value.length > 0) {
      this.expense.types.push(tokenInput.value);
      tokenInput.value = "";
    }
  }

  public expenseTypesOnSelect(value: string): void {
    const hasDuplicate: boolean = this.expense.types.indexOf(value) != -1;
    this.expense.types.pop();
    if (hasDuplicate) {
      this.expense.types.pop();
    }
    this.expense.types.push(value);
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
