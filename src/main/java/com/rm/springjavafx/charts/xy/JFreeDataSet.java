package com.rm.springjavafx.charts.xy;

/**
 *
 * @author Ricardo Marquez
 */
public interface JFreeDataSet {

  public void addOrUpdate(XYValues series);

  public int getSeriesIndex(String key);
  
}
