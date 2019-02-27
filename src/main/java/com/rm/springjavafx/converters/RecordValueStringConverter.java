package com.rm.springjavafx.converters;

import com.rm.datasources.RecordValue;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rmarquez
 */
public class RecordValueStringConverter implements Converter<RecordValue, Object>, InitializingBean {
  
  private String valueField;
  private ListProperty<RecordValue> list;
  
  
  public RecordValueStringConverter() {
  }
  
  public void setList(ListProperty<RecordValue> list) {
    this.list = list;
  }
  
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
  public Object convert(RecordValue source) {
    Object value = source.get(this.valueField);
    return value;
  }
  
  /**
   * 
   * @param ref
   * @return 
   */
  @Override
  public RecordValue deconvert(Object ref) {
    List<RecordValue> collect = this.list.getValue().stream()
            .filter((t) -> Objects.equals(t.get(valueField), ref))
            .collect(Collectors.toList());
    if (collect.size() > 1) {
      throw new RuntimeException("Invalid multiple results."); 
    } 
    RecordValue result;
    if (collect.isEmpty()) {
      result = null;
    } else {
      result = collect.get(0); 
    }
    return result;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
//    if (this.list == null) {
//      throw new RuntimeException("List cannot be null"); 
//    }
    if (this.valueField == null) {
      throw new RuntimeException("value field cannot be null."); 
    }
  }
  
}
