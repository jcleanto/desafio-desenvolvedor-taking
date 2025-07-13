import { HttpErrorResponse } from "@angular/common/http";
import { throwError } from "rxjs";

export function handleError(error: HttpErrorResponse, specificMessage: string) {
  let message = 'Erro inesperado. Por favor, tente novamente mais tarde.';
  if (error.status === 0) {
    // A client-side or network error occurred. Handle it accordingly.
    console.error(message, error.error);
  } else {
    // The backend returned an unsuccessful response code.
    // The response body may contain clues as to what went wrong.
    console.error(`Backend returned code ${error.status}, body was: `, error.error);
    if (error.error.details.indexOf('org.jboss.resteasy.spi.UnhandledException') !== -1) {
      message = specificMessage;
    }
  }
  // Return an observable with a user-facing error message.
  return throwError(() => new Error(message));
}