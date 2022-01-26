package com.erensayar.hrDayOffModuleApplication.error.controller;

import com.erensayar.hrDayOffModuleApplication.error.exception.*;
import com.erensayar.hrDayOffModuleApplication.error.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static com.erensayar.hrDayOffModuleApplication.error.exception.ExceptionConstants.CONFLICT_ERROR_CODE;
import static com.erensayar.hrDayOffModuleApplication.error.exception.ExceptionConstants.CONFLICT_ERROR_MESSAGE;
import static com.erensayar.hrDayOffModuleApplication.error.exception.ExceptionConstants.VALIDATION_ERROR_CODE;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException e) {
        return handle(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException e) {
        return handle(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException e) {
        return handle(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Object> handleNoContentException(NoContentException e) {
        return handle(e, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return handle(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OkWithMessage.class)
    public ResponseEntity<Object> OkWithMessage(OkWithMessage e) {
        return handle(e, HttpStatus.OK);
    }

    private ResponseEntity<Object> handle(BaseException e, HttpStatus h) {
        log.error(e.getErrorMessage(), e);

        String errorCode = e.getErrorCode();
        String errorMessage = e.getErrorMessage();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(CONTENT_TYPE, CONTENT_TYPE_VALUE);

        ErrorResponse responseBody = ErrorResponse.builder()
                .errorType(e.getClass().getName())
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();

        return ResponseEntity
                .status(h)
                .headers(responseHeaders)
                .body(responseBody);
    }


    // Validation Exception Handler
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidException(ConstraintViolationException e) {
        String errorCode = VALIDATION_ERROR_CODE;
        String errorMessage = null;
        String errorType = null;

        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
            log.error(cv.getMessage(), e);
            errorMessage = cv.getMessage();
            errorType = cv.getClass().getName();
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(CONTENT_TYPE, CONTENT_TYPE_VALUE);

        ErrorResponse responseBody = ErrorResponse.builder()
                .errorType(errorType)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(responseHeaders)
                .body(responseBody);
    }

    // Conflict Exception Handler. For Unique properties
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleUniqueException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);

        String errorCode = CONFLICT_ERROR_CODE;
        String errorMessage = CONFLICT_ERROR_MESSAGE;

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(CONTENT_TYPE, CONTENT_TYPE_VALUE);

        ErrorResponse responseBody = ErrorResponse.builder()
                .errorType(e.getClass().getName())
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(responseHeaders)
                .body(responseBody);
    }


}
