import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs";
import {AuthenticationService} from "../authentication/authentication.service";
import { map } from 'rxjs/operators';

@Injectable()
export class GuardService implements CanActivate {

  constructor(protected authenticationService: AuthenticationService,
              protected router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.authenticationService.getLoggedUser().pipe(
      map(res => {
          if (!res) {
            this.router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
            return false;
          }
          return true;
        },
        error => {
          this.router.navigate(['/login']);
          return false;
        })
    );
  }

}
