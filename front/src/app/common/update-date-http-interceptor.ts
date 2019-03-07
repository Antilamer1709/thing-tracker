import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";

@Injectable()
export class UpdateDateHttpInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.method === 'POST' || request.method === 'PUT') {
      this.shiftDates(request.body);
    }
    return next.handle(request);
  }

  shiftDates(body) {
    if (body === null || body === undefined) {
      return body;
    }

    if (typeof body !== 'object') {
      return body;
    }

    for (const key of Object.keys(body)) {
      const value = body[key];
      if (value instanceof Date) {
        console.log("UpdateDateHttpInterceptor: SHIFTING DATE");
        body[key] = new Date(Date.UTC(value.getFullYear(), value.getMonth(), value.getDate(), value.getHours(), value.getMinutes()
          , value.getSeconds()));
      } else if (typeof value === 'object') {
        this.shiftDates(value);
      }
    }
  }
}
