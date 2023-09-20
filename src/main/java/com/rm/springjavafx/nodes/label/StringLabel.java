package com.rm.springjavafx.nodes.label;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javafx.geometry.Pos;
import javafx.util.StringConverter;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringLabel {

  /**
   *
   * @return
   */
  String[] beanId();

  /**
   *
   * @return
   */
  Pos alignment() default Pos.CENTER_LEFT;

  /**
   * A bean convertor. The bean should be a type of {@link StringConverter}.
   *
   * @return
   */
  String converterBean() default "";

  /**
   * A convertor class. This should take an empty argument. This is only used if {@link #converterBean()
   * } is not used.
   *
   * @return
   */
  Class<? extends StringConverter> converterClass() default StringConverter.class;
}
