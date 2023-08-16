package com.philiphiliphilip.myportfolioapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    // Use as a " catch-all safety net"
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest webRequest) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), webRequest.getDescription(false));

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException usernameAlreadyExistsException){
        // Log the detailed error message for developers
        log.error(usernameAlreadyExistsException.getMessage(), usernameAlreadyExistsException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Username already taken"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException authenticationException){
        // Log the detailed error message for developers
        log.error(authenticationException.getMessage(), authenticationException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Invalid login credentials"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException userNotFoundException){
        // Log the detailed error message for developers
        log.error(userNotFoundException.getMessage(), userNotFoundException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Username doesn't exist."));
    }

    @ExceptionHandler(UserDeletionFailedException.class)
    public ResponseEntity<Object> handleUserDeletionFailed(UserDeletionFailedException userDeletionFailedException){
        // Log the deatiled error message for developers
        log.error(userDeletionFailedException.getMessage(), userDeletionFailedException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "User couldn't be deleted. " +
                "Try again or contact the developer."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException, WebRequest request){
        // Log the detailed error message for developers
        String path = request.getDescription(false);
        String detailedMessage = String.format("Access denied at %s: %s", path, accessDeniedException.getMessage());
        log.error(detailedMessage, accessDeniedException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "You don't have the needed " +
                "authorization to execute this request."));
    }
}
