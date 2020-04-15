/* tslint:disable */
// Generated using typescript-generator version 2.12.476 on 2020-04-15 18:45:34.

export class ExpenseDTO {
    id: number;
    price: number;
    comment: string;
    date: Date;
    creator: UserDTO;
    types: string[];
}

export class ExpenseSearchChartDTO {
    data: { [index: string]: number };
}

export class ExpenseSearchDTO {
    id: number;
    price: number;
    comment: string;
    dateFrom: Date;
    dateTo: Date;
    selectGroupmates: SelectGroupmateDTO[];
    selectGroupmateIds: number[];
    expenseTypes: string[];
}

export class GroupDTO {
    id: number;
    name: string;
    creator: UserDTO;
    users: UserDTO[];
}

export class HostDTO {
    hostName: string;
    redirectOAuthRUri: string;
}

export class JwtAuthenticationResponseDTO {
    accessToken: string;
    tokenType: string;
}

export class MessageDTO {
    id: number;
    message: string;
    sender: string;
    fromId: string;
    toId: string;
    loading: boolean;
}

export class RegistrationDTO {
    fullName: string;
    email: string;
    password: string;
    confirmPassword: string;
}

export class ResponseToMessageDTO {
    response: boolean;
    messageId: number;
}

export class SearchDTO<T> {
    first: number;
    rows: number;
    sortOrder: number;
    sortField: string;
    filter: T;
}

export class SelectGroupmateDTO {
    groupId: number;
    userId: number;
    label: string;
    type: GroupmateType;
}

export class UserDTO implements Serializable {
    id: number;
    email: string;
    fullName: string;
    fullNameAndEmail: string;
    password: string;
    roles: string[];
}

export class ResponseDTO<T> {
    totalElements: number;
    totalPages: number;
    data: T;
}

export class ResponseError {
    code: number;
    message: string;
    exception: Exception;
}

export interface Serializable {
}

export class Throwable implements Serializable {
    cause: Throwable;
    stackTrace: StackTraceElement[];
    message: string;
    suppressed: Throwable[];
    localizedMessage: string;
}

export class Exception extends Throwable {
}

export class StackTraceElement implements Serializable {
    classLoaderName: string;
    moduleName: string;
    moduleVersion: string;
    methodName: string;
    fileName: string;
    lineNumber: number;
    className: string;
    nativeMethod: boolean;
}

export const enum GroupmateType {
    USER,
    GROUP,
}
