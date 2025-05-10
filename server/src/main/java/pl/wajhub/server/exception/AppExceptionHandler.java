package pl.wajhub.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;


@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> exceptionHandler(EventNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CollectionBoxNotFoundException.class)
    public ResponseEntity<String> exceptionHandler(CollectionBoxNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectMoneyValueException.class)
    public ResponseEntity<String> exceptionHandler(IncorrectMoneyValueException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyCodeException.class)
    public ResponseEntity<String> exceptionHandler(CurrencyCodeException ex) {
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body("Validation error(s): " + errors);
    }

}
