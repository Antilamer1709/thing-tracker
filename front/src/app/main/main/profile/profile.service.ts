import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDTO} from "../../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) { }

  public getUser(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>('/api/user/' + id);
  }

}
