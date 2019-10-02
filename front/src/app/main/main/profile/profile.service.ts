import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ExpenseDTO, ExpenseSearchDTO, ResponseDTO, SearchDTO, UserDTO} from "../../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) { }

  public getUser(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>('/api/user/' + id);
  }

  public searchProfileExpenses(searchDTO: SearchDTO<ExpenseSearchDTO>): Observable<ResponseDTO<ExpenseDTO[]>> {
    return this.http.post<ResponseDTO<ExpenseDTO[]>>('/api/expense/search/profile', searchDTO);
  }

}
