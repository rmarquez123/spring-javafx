package com.rm.springjavafx.nodes.combobox;

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
public @interface ComboBoxConf {

  /**
   *
   * @return
   */
  String[] beanId() default "";
  
  /**
   *
   * @return
   */
  String[] listRef() default "";
  
}
