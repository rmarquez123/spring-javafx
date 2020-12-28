package com.rm.springjavafx.nodes.textfields.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.DecimalFormat;
import javafx.geometry.Pos;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumericTextField {

  /**
   * The number format.  For example '####.00'. Use # for integer values. 
   * @return
   * @see DecimalFormat
   */
  String format();

  /**
   * The reference value bean
   * @return
   */
  String[] beanId() default "";
  
  /**
   * The position of the text in the text field. 
   * @return 
   */
  Pos alignment() default Pos.CENTER_RIGHT;
}
