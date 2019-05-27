import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {GroupDTO, SelectGroupmateDTO, UserDTO} from "../../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }


  public saveGroup(group: GroupDTO): Observable<GroupDTO> {
    return this.http.post<GroupDTO>('/api/group', group);
  }

  public searchUserGroups(): Observable<GroupDTO[]> {
    return this.http.get<GroupDTO[]>('/api/group/search');
  }

  public getUserGroup(id: number): Observable<GroupDTO> {
    return this.http.get<GroupDTO>('/api/group/' + id);
  }

  public inviteUsersToGroup(id: number, users: UserDTO[]): Observable<void> {
    return this.http.post<void>('/api/group/' + id + '/inviteToGroup', users);
  }

  public getGroupmates(): Observable<SelectGroupmateDTO[]> {
    return this.http.get<SelectGroupmateDTO[]>('/api/group/groupmates');
  }

  public kickUserFromGroup(groupId: number, userId: number): Observable<void> {
    return this.http.post<void>('/api/group/' + groupId + '/kick/' + userId, {});
  }

}
