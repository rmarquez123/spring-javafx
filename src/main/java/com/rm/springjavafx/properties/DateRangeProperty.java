package com.rm.springjavafx.properties;

import java.util.Date;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author rmarquez
 */
public class DateRangeProperty extends ObjectPropertyBase<DateRange> {

  private final Property<Date> endDateProperty = new SimpleObjectProperty<>();
  private final Property<Date> startDateProperty = new SimpleObjectProperty<>();
  /**
   * 
   */
  public DateRangeProperty() {
    this(null);
  }
  
  /**
   *
   * @param dateRange
   */
  public DateRangeProperty(DateRange dateRange) {
    super(dateRange);
    this.startDateProperty.addListener((d, oldVal, newVal) -> {
      this.setValue(new DateRange(startDateProperty.getValue(), endDateProperty.getValue()));
    });
    this.endDateProperty.addListener((d, oldVal, newVal) -> {
      this.setValue(new DateRange(startDateProperty.getValue(), endDateProperty.getValue()));
    });
    this.addListener((obs, oldVal, newVal) -> {
      this.startDateProperty.setValue(newVal.getStartDate());
      this.endDateProperty.setValue(newVal.getEndDate());
    });
  }

  /**
   *
   * @return
   */
  @Override
  public Object getBean() {
    return null;
  }

  @Override
  public String getName() {
    return "";
  }

}
