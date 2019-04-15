import { Injectable } from '@angular/core';
import {environment} from "../../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {MessageDTO} from "../../../../../generated/dto";
import { Stomp} from 'stompjs/lib/stomp.js';
import SockJS from 'sockjs-client';
import {AuthRepository} from "../../../../authentication/repository/auth.repository";

@Injectable({
  providedIn: 'root'
})
export class UserMessageService {

  url: string = environment.apiUrl + "/api/socket";

  constructor(private http: HttpClient,
              private authRepo: AuthRepository) { }

  post(data: MessageDTO) {
    return this.http.post(this.url, data);
  }

  public connect(): any {
    let socket = new SockJS(environment.apiUrl + '/socket?jwt=' + this.authRepo.getJWT().accessToken);

    return Stomp.over(socket);
  }
}
