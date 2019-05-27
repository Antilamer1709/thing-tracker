import { Pipe, PipeTransform } from '@angular/core';
import {Roles} from "../enums/RoleEnum";

@Pipe({
  name: 'roles'
})
export class RolesPipe implements PipeTransform {

  transform(roles: string[], args?: any): string {
    let roleString = "";

    roles.forEach(role => {
      roleString += this.getEnumKeyByEnumValue(Roles, role);
      roleString += " ";
    });

    return roleString;
  }

  // Workaround for enum
  getEnumKeyByEnumValue(myEnum, enumValue) {
    let keys = Object.keys(Roles).filter(x => myEnum[x] == enumValue);
    return keys.length > 0 ? keys[0] : null;
  }

}
