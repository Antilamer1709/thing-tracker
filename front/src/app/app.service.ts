import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  blockedUI: boolean = false;

  constructor() {
  }

}
