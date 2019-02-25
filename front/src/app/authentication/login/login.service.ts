import {Injectable} from '@angular/core';
import {CommonService} from "../../common/common.service";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {UserDTO} from "../../../generated/dto";

@Injectable()
export class LoginService extends CommonService {

  constructor(private http: HttpClient) {
    super()
  }

  authenticate(login: UserDTO): Observable<any> {
    let urlSearchParams = new URLSearchParams();
    urlSearchParams.append('username', login.username);
    urlSearchParams.append('password', login.password);
    let body = urlSearchParams.toString();

    return this.http.post('api/authenticate', body, {headers: this.getEncodedHeaders()});
  }

}
