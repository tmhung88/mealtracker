package com.company.webservice.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocalTimeFormatValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalTimeFormat {
    String message() default "{app.validation.constraints.LocalTimeFormat.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean nullable() default true;
}
