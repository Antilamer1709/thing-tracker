import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ExpenseDTO, ExpenseSearchChartDTO, ExpenseSearchDTO} from "../../../../generated/dto";
import {CommonService} from "../../../common/common.service";

@Injectable({
  providedIn: 'root'
})
export class ExpensesService extends CommonService {

  constructor(private http: HttpClient) {
    super();
  }

  public saveExpense(expenseDTO: ExpenseDTO): Observable<void> {
    return this.http.post<void>('/api/expense', expenseDTO);
  }

  public deleteExpense(id: number): Observable<void> {
    return this.http.delete<void>('/api/expense/' + id);
  }

  public searchExpenseTypes(predicate: string): Observable<string[]> {
    return this.http.get<string[]>('/api/expense/types', {
      params: {
        predicate: predicate
      }
    });
  }

  public searchChartExpenses(searchDTO: ExpenseSearchDTO): Observable<ExpenseSearchChartDTO> {
    return this.http.post<ExpenseSearchChartDTO>('/api/expense/search/chart', searchDTO);
  }

}
