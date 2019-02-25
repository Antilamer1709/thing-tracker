export class UserDTO {
  id: number;
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

export class RegistrationModel {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  confirmPassword: string;
}
