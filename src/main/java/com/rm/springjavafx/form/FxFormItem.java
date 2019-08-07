package com.rm.springjavafx.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javafx.util.StringConverter;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FxFormItem {

  String id();
  String label();
  Class<? extends StringConverter> converter();
}
