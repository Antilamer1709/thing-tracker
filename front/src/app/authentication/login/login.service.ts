import {Injectable} from '@angular/core';
import {CommonService} from "../../common/common.service";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {JwtAuthenticationResponseDTO, UserDTO} from "../../../generated/dto";
import {map} from "rxjs/operators";
import {AuthRepository} from "../repository/auth.repository";

@Injectable()
export class LoginService extends CommonService {

  constructor(private http: HttpClient,
              private authRepo: AuthRepository) {
    super()
  }

  login(user: UserDTO): Observable<JwtAuthenticationResponseDTO> {
    return this.http.post<any>('api/authentication/login', {
      email: user.email,
      password: user.password
    })
      .pipe(map(jwt => {
        if (jwt && jwt.accessToken) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          this.authRepo.auth(user.email, jwt);
        }
        return jwt;
      }));
  }

}
