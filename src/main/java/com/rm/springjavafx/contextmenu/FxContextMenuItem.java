package com.rm.springjavafx.contextmenu;

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
public @interface FxContextMenuItem {

  /**
   *
   * @return
   */
  String contextMenuRef();

  /**
   *
   * @return
   */
  String label();

  /**
   *
   * @return
   */
  String popupId() default "";
  
}
