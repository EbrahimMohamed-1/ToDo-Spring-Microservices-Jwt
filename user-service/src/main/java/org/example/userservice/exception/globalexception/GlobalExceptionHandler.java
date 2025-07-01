
package org.example.userservice.exception.globalexception;
import io.jsonwebtoken.JwtException;
import org.example.userservice.exception.exceptiontype.AccountNotActivatedException;
import org.example.userservice.exception.exceptiontype.MissingTokenException;
import org.example.userservice.exception.exceptiontype.OtpInvalidException;
import org.example.userservice.exception.exceptiontype.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid or expired token: " + ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User error: " + ex.getMessage());
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<String> handleOtpException(OtpInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("OTP error: " + ex.getMessage());
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<String> handleAccountNotActivated(AccountNotActivatedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Account not activated: " + ex.getMessage());
    }
    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<String> handleMissingToken(MissingTokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request: " + ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}
