package com.rm.springjavafx.charts.category;

import java.util.Set;

/**
 *
 * @author Ricardo Marquez
 */
public interface JFreeCategoryDataSet {
  
  /**
   * 
   * @param series 
   */
  public void addOrUpdate(CategoryValues series);
  
  /**
   * 
   * @param key
   * @return 
   */
  public int getSeriesIndex(String key);
  
  /**
   * 
   * @param key
   * @return 
   */
  void setCategories(Set<String> key);
}
