package com.rm.springjavafx._00.ignore.tree.datasources;

import com.rm.datasources.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import java.util.Arrays;
import java.util.HashMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component("office.departments_datasource")
public class DepartmentDataSource extends AbstractDataSource<RecordValue> implements InitializingBean {

  @Override
  public void afterPropertiesSet() throws Exception {
    this.setRecords(Arrays.asList(
      new RecordValue("department_id", new HashMap<String, Object>() {
        {
          put("department_id", 0);
          put("department_name", "Warehouse");
          put("blank", "");
        }
      }),
      new RecordValue("department_id", new HashMap<String, Object>() {
        {
          put("department_id", 1);
          put("department_name", "Accounting");
          put("blank", "");
        }
      }),
      new RecordValue("department_id", new HashMap<String, Object>() {
        {
          put("department_id", 2);
          put("department_name", "Sales");
          put("blank", "");
        }
      })
    ));
  }

}
