package com.mealtracker.exceptions;

import com.mealtracker.payloads.ErrorEnvelop;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public @ResponseBody
    ErrorEnvelop handleNotFoundException(ResourceNotFoundException ex) {
        return new ErrorEnvelop(ex.getError());
    }
}
