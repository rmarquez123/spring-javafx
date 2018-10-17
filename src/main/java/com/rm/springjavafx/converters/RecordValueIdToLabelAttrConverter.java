package com.rm.springjavafx.converters;

/**
 *
 * @author rmarquez
 */
public class RecordValueIdToLabelAttrConverter implements Converter<Object, Object>{
  
  private String idField;
  private String valueField;
  
  public void setIdField(String idField) {
    this.idField = idField;
  }

  public void setValueField(String valueField) {
    this.valueField = valueField;
  }
  /**
   * 
   * @param source
   * @return 
   */
  @Override
  public Object convert(Object source) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
