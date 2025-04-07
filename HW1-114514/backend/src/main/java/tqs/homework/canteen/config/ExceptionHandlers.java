package tqs.homework.canteen.config;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.Builder;

@RestControllerAdvice
public class ExceptionHandlers {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

    @Builder
    static record ErrorResponse(String message, LocalDateTime timestamp) {}


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("No such element exception while completing request: {}", ex.getMessage());
        logger.error("Returning 404 Not Found");
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
        logger.error("Illegal argument exception while completing request: {}", ex.getMessage());
        logger.error("Returning 400 Bad Request");
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
        logger.error("Generic exception while completing request: {}", ex.getMessage(), ex);
        logger.error("Returning 500 Internal Server Error");
        
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
