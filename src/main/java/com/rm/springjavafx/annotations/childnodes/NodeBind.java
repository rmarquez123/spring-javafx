package com.rm.springjavafx.annotations.childnodes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
public class NodeBind {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  public static @interface CheckBox {

    /**
     *
     * @return
     */
    String selectedId() default "";
  }

}
