package com.rm.springjavafx.nodes.textfields.password;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javafx.geometry.Pos;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PasswordTextField {

  /**
   *
   * @return
   */
  String[] beanId() default "";

  /**
   *
   * @return
   */
  Pos alignment() default Pos.CENTER_LEFT;
}
