package com.rm.springjavafx.project;

import com.rm.springjavafx.project.NullStringConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Converter {
  
  Class<? extends AttributeConverter> converter() default NullStringConverter.class;
}
