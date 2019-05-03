package com.mealtracker.exceptions;

import com.mealtracker.payloads.ErrorEnvelop;
import com.mealtracker.payloads.ErrorField;
import com.mealtracker.security.jwt.JwtValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final ErrorIdGenerator generator;

    public GlobalExceptionHandler(ErrorIdGenerator generator) {
        this.generator = generator;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundAppException.class})
    public @ResponseBody
    ErrorEnvelop handleNotFoundException(ResourceNotFoundAppException ex) {
        return new ErrorEnvelop(ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Exception e)   {
        return new ModelAndView("404");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    ErrorEnvelop handleBadRequestException(MethodArgumentNotValidException ex) {
        var errorFields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorField(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorEnvelop(BadRequestAppException.commonBadInputsError(errorFields));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public @ResponseBody
    ErrorEnvelop handleBindingException(BindException ex) {
        var errorFields = ex.getFieldErrors().stream()
                .map(fieldError -> new ErrorField(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorEnvelop(BadRequestAppException.commonBadInputsError(errorFields));
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestAppException.class)
    public @ResponseBody
    ErrorEnvelop handleBadRequestException(BadRequestAppException ex) {
        return new ErrorEnvelop(ex);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class })
    public @ResponseBody
    ErrorEnvelop handleAuthenticationException(AuthenticationException ex) {
        return new ErrorEnvelop(AuthenticationAppException.missingToken());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({JwtValidationException.class })
    public @ResponseBody
    ErrorEnvelop handleJwtValidationException(JwtValidationException ex) {
        return new ErrorEnvelop(AuthenticationAppException.invalidJwtToken(ex));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({ AccessDeniedException.class })
    public @ResponseBody
    ErrorEnvelop handleAuthorizationException(AccessDeniedException ex, WebRequest request) {
        return new ErrorEnvelop(AuthorizationAppException.apiAccessDeniedError());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({ AuthorizationAppException.class })
    public @ResponseBody
    ErrorEnvelop handleAuthorizationException(AuthorizationAppException ex) {
        return new ErrorEnvelop(ex);
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, InternalAppException.class})
    public @ResponseBody
    ErrorEnvelop handleUnexpectedException(Exception ex) {
        String errorId = generator.generateUniqueId();
        log.error("Please investigate the error {}", errorId, ex);
        return new ErrorEnvelop(InternalAppException.unexpectException(errorId));
    }
}
