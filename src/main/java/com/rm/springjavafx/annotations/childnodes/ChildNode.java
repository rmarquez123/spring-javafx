package com.rm.springjavafx.annotations.childnodes;

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
public @interface ChildNode {
  /**
   *
   * @return
   */
  public String fxml() default "";

  /**
   *
   * @return
   */
  public String id();

}
