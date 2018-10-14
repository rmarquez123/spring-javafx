package com.rm.springjavafx.properties;

import java.util.Date;

/**
 *
 * @author rmarquez
 */
public class DateRange {
  private final Date startDate; 
  private final Date endDate;
  
  public DateRange(Date startDate, Date endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "DateRange{" + "startDate=" + startDate + ", endDate=" + endDate + '}';
  }
  
}
