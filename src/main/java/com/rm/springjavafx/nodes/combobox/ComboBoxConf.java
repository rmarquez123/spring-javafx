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
   * The spring bean id associated with the selected object.
   *
   * @return
   */
  String[] beanId() default "";

  /**
   * The spring bean id associated with the list of the combobox. The spring bean can be
   * a type of @ObservableList or @ObservableSet
   *
   * @return
   */
  String[] listRef() default "";

}
