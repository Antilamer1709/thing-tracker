import { Component, OnInit } from '@angular/core';
import {ExpensesService} from "../expenses.service";

@Component({
  selector: 'app-add-expense',
  templateUrl: './add-expense.component.html',
  styleUrls: ['./add-expense.component.scss']
})
export class AddExpenseComponent implements OnInit {

  constructor(private expensesService: ExpensesService) { }

  ngOnInit() {
  }


  texts: string[] = [];
  results: string[];

  search(event) {
    this.results = ["aaa", "bbb", "ccc"];
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.key == "Enter") {
      let tokenInput = event.srcElement as any;
      if (tokenInput.value) {
        this.texts.push(tokenInput.value);
        tokenInput.value = "";
      }
    }
  }

}
