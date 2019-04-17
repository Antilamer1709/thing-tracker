import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ResponseToMessageDTO, UserDTO} from "../../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public searchUserSuggestions(predicate: string): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>('/api/user/autocomplete', {
      params: {
        predicate: predicate
      }
    });
  }

  public respondToMessage(response: ResponseToMessageDTO): Observable<void> {
    return this.http.post<void>('/api/message/respond', response);
  }

}
