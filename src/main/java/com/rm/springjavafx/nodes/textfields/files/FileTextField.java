package com.rm.springjavafx.nodes.textfields.files;

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
public @interface FileTextField {
  
  /**
   * 
   * @return 
   */
  String[] beanId() default "";
  
  /**
   * 
   * @return 
   */
  String buttonRef();
  
  
  /**
   * 
   * @return 
   */
  Pos alignment() default Pos.CENTER_LEFT;
  
  /**
   * 
   * 
   * @return 
   */
  String[] extensions() default {};
  
  /**
   * 
   * @return 
   */
  String[] extensionNames() default {};
  
}
