import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ExpenseDTO} from "../../../generated/dto";
import {CommonService} from "../../common/common.service";

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

}
