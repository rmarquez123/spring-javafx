package com.rm.springjavafx.menu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FxMenuItem {

  /**
   *
   * @return
   */
  String fxml();

  /**
   *
   * @return
   */
  String id();

  /**
   *
   * @return
   */
  KeyCode code() default KeyCode.F1;
  
  /**
   *
   * @return
   */
  KeyModifier modifier() default KeyModifier.NONE;
}
