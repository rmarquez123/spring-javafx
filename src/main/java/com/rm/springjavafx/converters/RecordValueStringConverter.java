package com.rm.springjavafx.converters;

import com.rm.datasources.RecordValue;

/**
 *
 * @author rmarquez
 */
public class RecordValueStringConverter implements Converter<RecordValue, String>{
  
  private String valueField;
  
  /**
   * 
   * @param valueField 
   */
  public void setValueField(String valueField) {
    this.valueField = valueField;
  }
  /**
   * 
   * @param source
   * @return 
   */
  @Override
  public String convert(RecordValue source) {
    Object value = source.get(this.valueField);
    String result = String.valueOf(value);
    return result;
  }
  
}
