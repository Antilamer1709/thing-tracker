import { Injectable } from '@angular/core';
import {CommonService} from "../common/common.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {UserDTO} from "../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService extends CommonService {

  public loggedUser: UserDTO;

  constructor(private http: HttpClient) {
    super()
  }

  logout(): Observable<any> {
    return this.http.get('/api/logout', {headers: this.getHeaders()});
  }

  getLoggedUser(): Observable<UserDTO> {
    if (this.loggedUser) {
      return Observable.create(this.loggedUser);
    } else {
      return this.http.post<UserDTO>('/api/authentication/loggedUser', {headers: this.getEncodedHeaders()});
    }
  }

}
