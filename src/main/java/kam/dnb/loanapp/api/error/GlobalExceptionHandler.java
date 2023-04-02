package kam.dnb.loanapp.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(final RequestException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getFailureReason()), HttpStatus.BAD_REQUEST);
    }
}
