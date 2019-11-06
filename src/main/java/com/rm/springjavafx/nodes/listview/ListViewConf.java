package com.rm.springjavafx.nodes.listview;

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
public @interface ListViewConf {
  
  /**
   * 
   * @return 
   */
  public String listref();

  /**
   * 
   * @return 
   */
  public String selectedref();
  
}
