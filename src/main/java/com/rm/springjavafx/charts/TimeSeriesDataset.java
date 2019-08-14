package com.rm.springjavafx.charts;

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
public @interface TimeSeriesDataset {
  String chart();
  int dataset();
  String name();
  String lineColorHex() default "#000000";
  String visibilityProperty() default "";
}
