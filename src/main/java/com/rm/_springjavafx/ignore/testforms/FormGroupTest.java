package com.rm._springjavafx.ignore.testforms;

import common.db.RecordValue;
import com.rm.springjavafx.form.AbstractFormGroup;
import com.rm.springjavafx.form.FxFormGroup;
import com.rm.springjavafx.form.FxFormItem;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.Property;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@FxFormGroup(
  groupId = "personAttributes",
  label = "Person Attributes",
  recordId = "personId"
)
public class FormGroupTest extends AbstractFormGroup implements InitializingBean {
  
  @FxFormItem(id = "firstName", label = "First Name", converter = DefaultStringConverter.class)
  private Property<String> firstName;

  @FxFormItem(id = "lastName", label = "Last Name", converter =  DefaultStringConverter.class)
  private Property<String> lastName;

  @FxFormItem(id = "age", label = "Age", converter = IntegerStringConverter.class)
  private Property<String> age;
  
  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    RecordValue record0 = new RecordValue("personId", new HashMap<String, Object>() {
      {
        put("personId", 0);
        put("firstName", "Ricardo");
        put("lastName", "Marquez");
        put("age", 18);
      }
    }); 
    RecordValue record1 = new RecordValue("personId", new HashMap<String, Object>() {
      {
        put("personId", 1);
        put("firstName", "Michael");
        put("lastName", "Jackson");
        put("age", 18);
      }
    }); 
    RecordValue record2 = new RecordValue("personId", new HashMap<String, Object>() {
      {
        put("personId", 2);
        put("firstName", "Elvis");
        put("lastName", "Presly");
        put("age", 18);
      }
    });
    super.setRecords(Arrays.asList(record0, record1, record2));
    super.selected(2);
  }
  
  /**
   *
   * @param records
   */
  @Override
  public void onSaveAll(List<RecordValue> records) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void onSave(RecordValue records) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
