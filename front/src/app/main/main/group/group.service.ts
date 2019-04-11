import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {GroupDTO} from "../../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }


  public saveGroup(group: GroupDTO): Observable<GroupDTO> {
    return this.http.post<GroupDTO>('/api/group', group);
  }

}
