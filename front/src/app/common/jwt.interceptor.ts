import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import {AuthRepository} from "../authentication/repository/auth.repository";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authRepo: AuthRepository){}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const jwt = this.authRepo.getJWT();
        if (jwt && jwt.accessToken) {
            request = request.clone({
                setHeaders: {
                    'Authorization': `${jwt.tokenType} ${jwt.accessToken}`
                }
            });
        }
        request = request.clone({
            setHeaders: {
                'Access-Control-Allow-Origin': '*'
            }
        });

        return next.handle(request);
    }
}
