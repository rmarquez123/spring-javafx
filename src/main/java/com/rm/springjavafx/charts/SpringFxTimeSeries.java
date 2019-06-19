package com.rm.springjavafx.charts;

import org.jfree.data.time.TimeSeries;

/**
 *
 * @author Ricardo Marquez
 */
public class SpringFxTimeSeries extends TimeSeries{
  
  /**
   * 
   */
  public SpringFxTimeSeries() {
    super("");
    this.setKey(this.getClass().getDeclaredAnnotation(TimeSeriesDataset.class).name());
  }
  
}
