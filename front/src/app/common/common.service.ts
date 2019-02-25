import {HttpHeaders} from "@angular/common/http";

export class CommonService {

  constructor() {
  }

  protected getEncodedHeaders(): HttpHeaders {
    return new HttpHeaders(
      {'Content-Type': 'application/x-www-form-urlencoded'}
    );
  }

  protected getHeaders(): HttpHeaders {
    return new HttpHeaders(
      {
        'Content-Type': 'application/json',
        'Pragma': 'no-cache',
        'Cache-Control': 'no-cache'
      }
    );
  }

}
