package com.rm.springjavafx.nodes.spinner;

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
public @interface SpinnerConf {

  /**
   *
   * @return
   */
  String[] beanId() default "";
  
  /**
   * 
   * @return 
   */
  int min();
  
  /**
   * 
   * @return 
   */
  int max(); 
}
