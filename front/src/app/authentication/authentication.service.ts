import { Injectable } from '@angular/core';
import {CommonService} from "../common/common.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {UserDTO} from "../../generated/dto";
import {AuthRepository} from "./repository/auth.repository";
import {Roles} from "../common/enums/RoleEnum";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService extends CommonService {

  public loggedUser: UserDTO;

  constructor(private http: HttpClient,
              private authRepo: AuthRepository) {
    super()
  }

  logout(): void {
    this.authRepo.clearAuth();
    this.loggedUser = null;
  }

  getLoggedUser(): Observable<UserDTO> {
    if (this.loggedUser) {
      return Observable.create((observer) => {
        observer.next(this.loggedUser);
      });
    } else {
      return this.http.post<UserDTO>('/api/authentication/loggedUser', {headers: this.getEncodedHeaders()});
    }
  }

  public initLoggedUser(): void {
    this.loggedUser = null;
    this.getLoggedUser().subscribe(
      res => {
        console.log("loggedUser: ");
        console.log(res);
        this.loggedUser = res;
      }
    );
  }

  public hasRole(role: Roles): boolean {
    return this.loggedUser.roles.includes(role.toString());
  }

}
