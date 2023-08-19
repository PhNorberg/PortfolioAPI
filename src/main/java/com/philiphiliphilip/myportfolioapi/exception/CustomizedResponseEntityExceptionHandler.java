package com.philiphiliphilip.myportfolioapi.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    @ExceptionHandler(PortfolioNameNotAcceptedException.class)
    public ResponseEntity<Object> handlePortfolioNameNotAcceptedException(PortfolioNameNotAcceptedException portfolioNameNotAcceptedException){
        // Log the detailed error message for developers
        log.error(portfolioNameNotAcceptedException.getMessage(), portfolioNameNotAcceptedException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message" ,
                "Invalid portfolio creation name. No special characters, spaces at the end nor beginning " +
                        "or double spaces allowed."));
    }

    @ExceptionHandler(PortfolioNameAlreadyExistsException.class)
    public ResponseEntity<Object> handlePortfolioNameAlreadyExistsException(PortfolioNameAlreadyExistsException portfolioNameAlreadyExistsException){
        // Log the detailed erorr message for developers
        log.error(portfolioNameAlreadyExistsException.getMessage(), portfolioNameAlreadyExistsException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "You already created a portfolio " +
                "with this name. Try again with another name."));
    }

    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<Object> handlePortfolioNotFoundException(PortfolioNotFoundException portfolioNotFoundException){
        // Log the detailed error message for developers
        log.error(portfolioNotFoundException.getMessage(), portfolioNotFoundException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",
                "Portfolio with this name not found. Try again"));
    }

    @ExceptionHandler(PortfolioDeletionFailedException.class)
    public ResponseEntity<Object> handlePortfolioDeletionFailedException(PortfolioDeletionFailedException portfolioDeletionFailedException){
        // Log the detailed error message for developers
        log.error(portfolioDeletionFailedException.getMessage(), portfolioDeletionFailedException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Portfolio couldn't be deleted. " +
                "Try again or contact the developer."));
    }

    @ExceptionHandler(AssetAlreadyExistsException.class)
    public ResponseEntity<Object> handleAssetAlreadyExistsException(AssetAlreadyExistsException assetAlreadyExistsException){
        // Log the detailed error message for developers
        log.error(assetAlreadyExistsException.getMessage(), assetAlreadyExistsException);

        // Return a message to the user
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Asset couldnt be created. You " +
                "have an asset named that already."));
    }

//    @ExceptionHandler(AssetNotFoundException.class)
//    public ResponseEntity<Object> handleAssetNotFoundException(AssetNotFoundException assetNotFoundException){
//        // Log the detailed error message for developers
//        log.error(assetNotFoundException.getMessage(), assetNotFoundException);
//
//        // Return a message to the user
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Asset not found."));
//    }

//    @ExceptionHandler(AssetQuantityNotEnoughException.class)
//    public ResponseEntity<Object> handleAssetQuantityNotEnoughException(AssetQuantityNotEnoughException assetQuantityNotEnoughException){
//        // Log the detailed error message for developers
//        log.error(assetQuantityNotEnoughException.getMessage(), assetQuantityNotEnoughException);
//
//        // Return a message to the user
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Asset quantity" +
//                "to sell is too big."));
//    }
    //@ExceptionHandler(MethodArgumentNotValidException.class)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request){
        // Log the detailed error message for developers
        log.error(ex.getMessage(), ex);
        log.error("WE ARE INSIDE THE HANDLEMETHODARGUMENTNOTVALID LMEOW");
        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> Objects.toString(error.getDefaultMessage(), ""),
                        // binary operator that concatenas errors if two or more validation errors struck
                        (oldValue, newValue) -> oldValue + "; " + newValue));
        return ResponseEntity.badRequest().body(errors);
        }
    }

