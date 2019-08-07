package com.rm.springjavafx.form;

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
public @interface FxForm {

  /**
   * 
   * @return 
   */
  String fxml();

  /**
   * 
   * @return 
   */
  String componentId();

  /**
   * 
   * @return 
   */
  String formId();

  /**
   * 
   * @return 
   */
  FxFormGroupId[] group();

}
