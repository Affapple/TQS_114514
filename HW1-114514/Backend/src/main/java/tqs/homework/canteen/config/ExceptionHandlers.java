package tqs.homework.canteen.config;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.Builder;

@RestControllerAdvice
public class ExceptionHandlers {
    @Builder
    static record ErrorResponse(String message, LocalDateTime timestamp) {}


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
