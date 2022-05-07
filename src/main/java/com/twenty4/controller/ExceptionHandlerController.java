package com.twenty4.controller;

import com.twenty4.common.ErrorResponseObject;
import com.twenty4.common.ResponseObject;
import com.twenty4.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerController {

    private HttpServletRequest request;
    private MessageSource messageSource;

    public ExceptionHandlerController(HttpServletRequest request,
                                      MessageSource messageSource) {
        this.request = request;
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerValidateException(MethodArgumentNotValidException exception){
        String methodName = "handlerValidateException";

        log.error("{} -> Error fields: {}", methodName, exception.getCause(), exception);
        ResponseObject responseObject = new ResponseObject();
        responseObject.prepareHttpStatus(HttpStatus.BAD_REQUEST);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        responseObject.setData(errors);
        log.error("{} -> Error fields: {} response status: {}", methodName, errors, responseObject.getStatus());
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalException.class)
    public ResponseEntity handleInternalServerErrorException(InternalException internalException){
        String methodName = "handleInternalServerErrorException";

        log.error("{} -> Internal Exception: {}", methodName, internalException.getCause(), internalException);
        ErrorResponseObject errorResponseObject = new ErrorResponseObject();
        errorResponseObject.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        errorResponseObject.setStatus(500);
        errorResponseObject.setMessage(internalException.getMessage());
        errorResponseObject.setPath(request.getRequestURI());
        errorResponseObject.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        log.error("{} -> Internal Exception: response status: {}", methodName, errorResponseObject.getStatus());
        return ResponseEntity.status(500).body(errorResponseObject);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException accessDeniedException){
        String methodName = "handleInternalServerErrorException";

        log.error("{} -> Access Denied Exception: {}", methodName, accessDeniedException.getCause(), accessDeniedException);
        ErrorResponseObject errorResponseObject = new ErrorResponseObject();
        errorResponseObject.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        errorResponseObject.setStatus(403);
        errorResponseObject.setMessage(accessDeniedException.getMessage());
        errorResponseObject.setPath(request.getRequestURI());
        errorResponseObject.setError(HttpStatus.FORBIDDEN.getReasonPhrase());

        log.error("{} -> Access Denied Exception: response status: {}", methodName, errorResponseObject.getStatus());
        return ResponseEntity.status(403).body(errorResponseObject);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestApiException.class})
    public ResponseEntity handleBadRequestApiException(BadRequestApiException ex, Locale locale) {
        String methodName = "handleBadRequestApiException";

        log.error("{} -> API Exception - Bad Request: {}", methodName, ex.getCause(), ex);
        ErrorResponseObject errorResponse = new ErrorResponseObject();

        errorResponse.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(ex.getStatusCode().value());
        errorResponse.setMessage(ex.getErrorCode().getReasonPhrase());
        errorResponse.setError(ex.getStatusCode().getReasonPhrase());

        log.error("{} -> API Exception: response status: {}", methodName, errorResponse.getStatus());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundApiException.class})
    public ResponseEntity handleNotFoundApiException(NotFoundApiException ex, Locale locale) {
        String methodName = "handleNotFoundApiException";

        log.error("{} -> API Exception - Not Found: {}", methodName, ex.getCause(), ex);
        ErrorResponseObject errorResponse = new ErrorResponseObject();

        String message;

        if(ex.getParameters()!=null) {
            message = this.messageSource.getMessage(ex.getErrorCode().getReasonPhrase(), ex.getParameters(), locale);
        } else {
            message = ex.getErrorCode().getMessageTitleKey();
        }

        errorResponse.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(ex.getStatusCode().value());
        errorResponse.setMessage(message);
        errorResponse.setError(ex.getStatusCode().getReasonPhrase());

        log.error("{} -> API Exception: response status: {}", methodName, errorResponse.getStatus());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity handleConflictException(ConflictException ex, Locale locale) {
        String methodName = "handleConflictException";

        log.error("{} -> API Exception - Conflict: {}", methodName, ex.getCause(), ex);
        ErrorResponseObject errorResponse = new ErrorResponseObject();

        String message;

        if(ex.getParameters()!=null) {
            message = this.messageSource.getMessage(ex.getErrorCode().getReasonPhrase(), ex.getParameters(), locale);
        } else {
            message = ex.getErrorCode().getReasonPhrase();
        }

        errorResponse.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(ex.getStatusCode().value());
        errorResponse.setMessage(message);
        errorResponse.setError(ex.getStatusCode().getReasonPhrase());

        log.error("{} -> API Exception: response status: {}", methodName, errorResponse.getStatus());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity handleApiException(ApiException ex, Locale locale) {
        String methodName = "handleApiException";

        log.error("{} -> API Exception - Internal Server Error: {}", methodName, ex.getCause(), ex);
        ErrorResponseObject errorResponse = new ErrorResponseObject();

        String message = this.messageSource.getMessage(ex.getErrorCode().getMessageTitleKey(), ex.getParameters(), locale);

        errorResponse.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(ex.getErrorCode().getCode());
        errorResponse.setMessage(message);
        errorResponse.setError(ex.getErrorCode().getReasonPhrase());

        log.error("{} -> API Exception: response status: {}", methodName, errorResponse.getStatus());
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }
}
