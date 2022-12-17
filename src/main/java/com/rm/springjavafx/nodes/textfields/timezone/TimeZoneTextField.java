package com.rm.springjavafx.nodes.textfields.timezone;

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
public @interface TimeZoneTextField {

  /**
   * The reference value bean
   * @return
   */
  String[] beanId() default "";
  
  /**
   * The position of the text in the text field. 
   * @return 
   */
  Pos alignment() default Pos.CENTER_LEFT;
}
