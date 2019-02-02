package com.rm.springjavafx.properties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author rmarquez
 */
public class DateRange {

  private final Date startDate;
  private final Date endDate;
  public final static DateRange EMPTY = new DateRange(null, null);

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
   * @param dateRange
   * @return 
   */
  public static boolean isEmpty(DateRange dateRange) {
    return dateRange == EMPTY;
  }
  
  /**
   * 
   * @return 
   */
  public static DateRange Empty(){
    return EMPTY; 
  }
  
  /**
   * 
   * @param startDtText
   * @param endDtText
   * @param format
   * @return 
   */
  public static DateRange create(String startDtText, String endDtText, String format){
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    Date startDt; 
    try {
      startDt = formatter.parse(startDtText);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    Date endDt; 
    try {
      endDt = formatter.parse(endDtText);
    } catch (ParseException ex) {
      throw new RuntimeException(ex); 
    }
    DateRange result = new DateRange(startDt, endDt); 
    return result; 
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + Objects.hashCode(this.startDate);
    hash = 61 * hash + Objects.hashCode(this.endDate);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DateRange other = (DateRange) obj;
    if (!Objects.equals(this.startDate, other.startDate)) {
      return false;
    }
    if (!Objects.equals(this.endDate, other.endDate)) {
      return false;
    }
    return true;
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
