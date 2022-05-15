package com.asaf.runtime.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdValid {

  String message() default "could not find id for field";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String field() default "";

  Class<?> fieldType() default Void.class;

  String targetField() default "";

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    IdValid[] value();
  }
}
