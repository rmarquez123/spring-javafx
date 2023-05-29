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
public @interface Publish {
  
  /**
   * 
   * @return 
   */
  String value(); 
  
  /**
   * 
   * @return 
   */
  Subscribe.Type type() default Subscribe.Type.PROPERTIES;
  
  Publish.Thread thread() default Publish.Thread.SAME;
  
  String processflag() default "";
  
  public static enum Thread  {
    SAME, NEW, NEW_THEN_JFXPLATFORM
  }
}
