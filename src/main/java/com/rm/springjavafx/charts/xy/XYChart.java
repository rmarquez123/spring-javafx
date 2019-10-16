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
@Target(ElementType.TYPE)
public @interface XYChart {

  /**
   * The chart id which is used for reference from other datas sets.
   *
   * @return
   * @see TimeSeriesDataset
   */
  String id();

  /**
   * The node id of the Javafx pane. Classes with this annotation should also be annotated
   * with {@link FxController} in order and specify the fxml property through
   * {@link FxController#fxml()}.
   *
   * @return
   */
  String node();

  /**
   * I have no idea what this is.
   *
   * @return
   */
  XYDataSetGroup[] datasetgroups();

}
