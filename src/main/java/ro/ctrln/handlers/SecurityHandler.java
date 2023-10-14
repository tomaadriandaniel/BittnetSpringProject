package ro.ctrln.handlers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.ctrln.exceptions.InvalidCustomerIdException;
import ro.ctrln.exceptions.InvalidOperationException;

@ControllerAdvice
public class SecurityHandler {

    @ExceptionHandler(InvalidCustomerIdException.class)
    public ResponseEntity<String> handleInvalidCustomerIdException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer ID is not valid!");
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOperationException(InvalidOperationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }


}
