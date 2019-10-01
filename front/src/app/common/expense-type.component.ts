import {ExpensesService} from "../main/main/expenses/expenses.service";
import {CommonComponent} from "./common-component";

export class ExpenseTypeComponent extends CommonComponent {

  public expenseTypes: string[];
  public selectedExpenseTypes: string[];

  constructor(protected service: ExpensesService) {
    super();
    console.log("ExpenseTypeComponent");

    this.expenseTypes = [];
    this.selectedExpenseTypes = [];
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
    console.log('expenseTypesOnBlur');
    this.addAutocompleteValue(event);
  }

  private addAutocompleteValue(event): void {
    console.log('addAutocompleteValue');
    let tokenInput = event.srcElement as any;
    const value = tokenInput.value;
    if (value && value.length) {
      this.selectedExpenseTypes.push(tokenInput.value);
      tokenInput.value = "";
    }
  }

  public expenseTypesOnSelect(value: string): void {
    console.log('expenseTypesOnSelect');
    const hasDuplicate: boolean = this.selectedExpenseTypes.indexOf(value) != -1;
    this.selectedExpenseTypes.pop();
    if (hasDuplicate) {
      this.selectedExpenseTypes.pop();
    }
    this.selectedExpenseTypes.push(value);
  }

  public onUnselect(value: string): void {
    const index = this.selectedExpenseTypes.indexOf(value, 0);
    if (index > -1) {
      this.selectedExpenseTypes.splice(index, 1);
    }
  }

}
