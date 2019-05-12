package com.mealtracker.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValueInListValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueInList {

    String message() default "{app.validation.constraints.ValueInList.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    String[] value();
}
