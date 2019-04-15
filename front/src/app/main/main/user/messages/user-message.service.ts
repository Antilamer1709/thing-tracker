import { Injectable } from '@angular/core';
import {environment} from "../../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {MessageDTO} from "../../../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class UserMessageService {

  url: string = environment.apiUrl + "/api/socket";

  constructor(private http: HttpClient) { }

  post(data: MessageDTO) {
    return this.http.post(this.url, data);
  }
}
