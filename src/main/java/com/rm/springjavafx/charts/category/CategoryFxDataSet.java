package com.rm.springjavafx.charts.category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CategoryFxDataSet {
  
  /**
   * 
   * @return 
   */
  String chart();
  
  /**
   * 
   * @return 
   */
  int dataset();
  
  /**
   * 
   * @return 
   */
  String name();
  
  /**
   * 
   * @return 
   */
  String lineColorHex() default "#000000";
  
  /**
   * 
   * @return 
   */
  String visibilityProperty() default "";
}
