import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {CommonService} from "../../common/common.service";
import {RegistrationDTO} from "../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class RegistrationService extends CommonService {

  constructor(private http: HttpClient) {
    super();
  }

  register(registration: RegistrationDTO): Observable<any> {
    return this.http.get('api/oauth2/authorize/facebook?redirect_uri=http://localhost:4200/#/login', {headers: this.getHeaders()});
  }

}
