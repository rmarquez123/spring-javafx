package com.rm.springjavafx.charts.category.datasets;

import java.util.List;
import org.apache.commons.lang3.Range;

/**
 *
 * @author Ricardo Marquez
 */
public interface HasRanges<T> {
  
  /**
   * 
   * @return 
   */
  public List<Range<T>> getRange(); 
}
