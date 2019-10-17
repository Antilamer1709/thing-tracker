import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import { Stomp} from 'stompjs/lib/stomp.js';
import SockJS from 'sockjs-client';
import {AuthRepository} from "../../authentication/repository/auth.repository";
import {Observable} from "rxjs";
import {MessageDTO} from "../../../generated/dto";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class MainMenuService {

  constructor(private http: HttpClient,
              private authRepo: AuthRepository) { }


  public connect(): any {
    const jwt = this.authRepo.getJWT();
    if (jwt) {
      let socket = new SockJS(environment.apiUrl + '/socket?jwt=' + this.authRepo.getJWT().accessToken);

      return Stomp.over(socket);
    }
  }

  public getUserMessages(): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>('/api/message');
  }

  public getGroupInvites(): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>('/api/group/invites');
  }

}
