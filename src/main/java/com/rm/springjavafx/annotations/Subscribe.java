package com.rm.springjavafx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    
  /**
   * 
   * @return 
   */
  public String[] dependencies() default {}; 
  
  public Type bindtype() default Type.PROPERTIES;
  
  
  public static enum Type {
    PROPERTIES, LIST
  }
}
