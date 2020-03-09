package com.rm.springjavafx.nodes.button;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FxButton {

  /**
   * 
   * @return 
   */
  String onActionBtn() default "";
  
  /**
   * 
   * @return 
   */
  String onActionPopup() default "";
  
}
