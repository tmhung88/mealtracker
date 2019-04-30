package com.mealtracker.exceptions;

import com.mealtracker.payloads.Error;
import com.mealtracker.payloads.ErrorEnvelop;
import com.mealtracker.payloads.ErrorField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final ErrorIdGenerator generator;

    public GlobalExceptionHandler(ErrorIdGenerator generator) {
        this.generator = generator;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public @ResponseBody
    ErrorEnvelop handleNotFoundException(ResourceNotFoundException ex) {
        return new ErrorEnvelop(ex.getError());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    ErrorEnvelop handleLibraryValidationException(MethodArgumentNotValidException ex) {
        var errorFields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorField(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorEnvelop(Error.of(40000, "Invalid Input", errorFields));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody
    ErrorEnvelop handleAppValidationException(BadRequestException ex) {
        return new ErrorEnvelop(ex.getError());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class })
    public @ResponseBody
    ErrorEnvelop handleAuthenticationException(AuthenticationException ex) {
        log.info("40100 - You shall not pass!");
        return new ErrorEnvelop(Error.of(40100, "No authentication token found. Please look at this document"));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({ AccessDeniedException.class })
    public @ResponseBody
    ErrorEnvelop handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.info("40300 - You shall not pass!");
        return new ErrorEnvelop(Error.of(40300, "You are not allowed to use this api"));
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, InternalException.class})
    public @ResponseBody
    ErrorEnvelop handleException(Exception ex) {
        String errorId = generator.generateUniqueId();
        log.error("Please investigate the error {}", errorId, ex);
        String errorMessage = String.format("Oops! Please send us this id %s. We love to know what magic happened to you!", errorId);
        return new ErrorEnvelop(Error.of(50000, errorMessage));
    }




}
