package com.rm.springjavafx.charts.xy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface XYDataSetGroup {

  /**
   *
   * @return
   */
  PlotType plotType() default PlotType.LINE_PLOT;

  /**
   * The width of the bars.
   *
   * @return
   */
  double barwidth() default  0;
}
