package com.philiphiliphilip.myportfolioapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    // Use as a " catch-all safety net"
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest webRequest) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), webRequest.getDescription(false));

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRegistrationFormException.class)
    public ResponseEntity<Object> handleInvalidRegistrationFormException(InvalidRegistrationFormException exception){
        // Log error to developers
        logError(exception);

        // Get binding result errors and return those to the user.
        Map<String, String> errors = getErrors(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(InvalidLoginFormException.class)
    public ResponseEntity<Object> handleInvalidLoginFormException(InvalidLoginFormException exception){
        // Log error to developers
        logError(exception);

        // Get binding result errors and return those to the user.
        Map<String, String> errors = getErrors(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Invalid login credentials"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request){
        // Log the detailed error message for developers
        String path = request.getDescription(false);
        String detailedMessage = String.format("Access denied at %s: %s", path, exception.getMessage());
        log.error(detailedMessage, exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(PortfolioNameNotAcceptedException.class)
    public ResponseEntity<Object> handlePortfolioNameNotAcceptedException(PortfolioNameNotAcceptedException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message" ,
                exception.getToUser()));
    }

    @ExceptionHandler(PortfolioNameAlreadyExistsException.class)
    public ResponseEntity<Object> handlePortfolioNameAlreadyExistsException(PortfolioNameAlreadyExistsException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", exception.getToUser()));
    }

    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<Object> handlePortfolioNotFoundException(PortfolioNotFoundException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",
                exception.getToUser()));
    }

    @ExceptionHandler(AssetAlreadyExistsException.class)
    public ResponseEntity<Object> handleAssetAlreadyExistsException(AssetAlreadyExistsException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", exception.getToUser()));
    }
    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<Object> handleAssetNotFoundException(AssetNotFoundException exception) {
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getToUser()));
    }

    @ExceptionHandler(AssetQuantityNotEnoughException.class)
    public ResponseEntity<Object> handleAssetQuantityNotEnoughException(AssetQuantityNotEnoughException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", exception.getToUser()));
    }

    @ExceptionHandler(EmptyApiKeySetException.class)
    public ResponseEntity<Object> handleEmptyApiKeySetException(EmptyApiKeySetException exception){
        // Log error to developers
        logError(exception);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", exception.getToUser()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request){
        // Log the detailed error message for developers
        log.error(exception.getMessage(), exception);
        BindingResult bindingResult = exception.getBindingResult();

        Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> Objects.toString(error.getDefaultMessage(), ""),
                        // binary operator that concatenas errors if two or more validation errors struck
                        (oldValue, newValue) -> oldValue + "; " + newValue));
        return ResponseEntity.badRequest().body(errors);
        }

    private Map<String, String> getErrors(BindingResultException exception){
        return exception.getBindingResult().getFieldErrors().stream().
                collect(Collectors.toMap(FieldError::getField,
                        error -> Objects.toString(error.getDefaultMessage(), ""),
                        (oldValue, newValue) -> oldValue + "; " + newValue));
    }

    private void logError(RuntimeException exception){
        log.error(exception.getMessage(), exception);
    }

    }

