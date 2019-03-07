import { Injectable } from '@angular/core';
import {JwtAuthenticationResponseDTO} from "../../../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class AuthRepository {

  private JWT_KEY = 'jwt';
  private CURRENT_USER_KEY = 'currentUser';

  constructor() { }

  public getJWT(): JwtAuthenticationResponseDTO {
    return JSON.parse(localStorage.getItem(this.JWT_KEY));
  }

  public clearAuth(): void {
    localStorage.removeItem(this.JWT_KEY);
    localStorage.removeItem(this.CURRENT_USER_KEY);
  }

  public auth(username: string, jwt: JwtAuthenticationResponseDTO) {
    localStorage.setItem(this.CURRENT_USER_KEY, username);
    localStorage.setItem(this.JWT_KEY, JSON.stringify(jwt));
  }
}
