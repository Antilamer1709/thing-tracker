import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent, HttpResponse
} from '@angular/common/http';

import {Injectable, Injector} from "@angular/core";
import {MessageService} from "primeng";
import {Observable} from "rxjs/internal/Observable";
import {finalize, tap} from "rxjs/operators";
import {AppService} from "../app.service";

@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {

  messageService: MessageService;
  appService: AppService;

  constructor(
    private injector: Injector
  ) {

  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let ok: string;
    let response;

    if (!this.messageService)
      this.messageService = this.injector.get(MessageService); // get it here within intercept
    if (!this.appService)
      this.appService = this.injector.get(AppService);

    return next.handle(request)
      .pipe(
        tap(
          // Succeeds when there is a response; ignore other events
          event => ok = event instanceof HttpResponse ? 'succeeded' : '',
          // Operation failed; error is an HttpErrorResponse
          error => {
            ok = 'failed'
            response = error;
          }
        ),
        // Log when response observable either completes or errors
        finalize(() => {
          if (ok === 'failed') {
            this.appService.blockedUI = false;
            if (response.status === 401) {
              this.messageService.add({severity: 'error', summary: 'Error', detail: 'Authentication failed!'});
            } else if (response.status === 403) {
              this.messageService.add({severity: 'error', summary: 'Error', detail: 'Permission denied!'});
            } else if (response.status === 500) {
              this.messageService.add({severity: 'error', summary: 'Error', detail: response.error.message});
            } else {
              this.messageService.add({severity: 'error', summary: 'Error', detail: 'Something happaned!'});
            }
          }
        })
      );
  }
}
