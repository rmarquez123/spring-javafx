package com.rm.springjavafx.other;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NullIfNull {

  /**
   * The name of the reference bean to bind null values with.
   *
   * @return
   */
  public String refBean();

}
