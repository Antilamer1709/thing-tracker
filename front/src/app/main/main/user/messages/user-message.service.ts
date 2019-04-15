import { Injectable } from '@angular/core';
import {environment} from "../../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Message} from "./message-model";

@Injectable({
  providedIn: 'root'
})
export class UserMessageService {

  url: string = environment.apiUrl + "/api/socket";

  constructor(private http: HttpClient) { }

  post(data: Message) {
    return this.http.post(this.url, data);
  }
}
