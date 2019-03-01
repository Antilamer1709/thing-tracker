import { Component, OnInit } from '@angular/core';
import {ExpensesService} from "../expenses.service";
import {FormGroup} from "@angular/forms";
import {ExpenseDTO} from "../../../../generated/dto";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-add-expense',
  templateUrl: './add-expense.component.html',
  styleUrls: ['./add-expense.component.scss']
})
export class AddExpenseComponent implements OnInit {

  public expense: ExpenseDTO;
  public results: string[];

  constructor(private messageService: MessageService,
              private service: ExpensesService) { }

  ngOnInit() {
    this.createNewExpense();
    this.results = [];
  }

  private createNewExpense(): void {
    this.expense = new ExpenseDTO();
    this.expense.types = [];
  }

  search(event) {
    this.service.searchExpenseTypes(event.query).subscribe(
      (res) => {
        console.log(res);
        this.results = res;
      }
    );
  }

  public onKeyUp(event: KeyboardEvent): void {
    if (event.key == "Enter") {
      this.addAutocompleteValue(event);
    }
  }

  public onBlur(event: FocusEvent): void {
    this.addAutocompleteValue(event);
  }

  private addAutocompleteValue(event): void {
    let tokenInput = event.srcElement as any;
    if (tokenInput.value && tokenInput.value.length > 0) {
      this.expense.types.push(tokenInput.value);
      tokenInput.value = "";
    }
  }

  public saveExpense(form: FormGroup): void {
    if (form.valid) {
      this.service.saveExpense(this.expense).subscribe(
        (res) => {
          console.log(res);
          this.createNewExpense();
          this.messageService.add({severity:'info', summary:'Success', detail:'The expense has been saved!'});
        }
      );
    } else {
      this.messageService.add({severity:'error', summary:'Error', detail:'Please, fill all fields in correct way!'});
    }
  }

}
