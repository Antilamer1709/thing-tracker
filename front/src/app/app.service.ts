import {Injectable} from '@angular/core';
import {HostDTO} from "../generated/dto";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  public blockedUI: boolean = false;

  public host: HostDTO;

  constructor() {
  }

}
