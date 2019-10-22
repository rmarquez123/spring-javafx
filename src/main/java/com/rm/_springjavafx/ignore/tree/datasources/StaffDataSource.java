package com.rm._springjavafx.ignore.tree.datasources;

import common.db.RecordValue;
import com.rm.springjavafx.datasources.AbstractDataSource;
import java.util.Arrays;
import java.util.HashMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component("office.staff_datasource")
public class StaffDataSource extends AbstractDataSource<RecordValue> implements InitializingBean {

  @Override
  public void afterPropertiesSet() throws Exception {
    this.setRecords(Arrays.asList(
      new RecordValue("department_id", new HashMap<String, Object>() {
        {
          put("staff_id", 0);
          put("department_id", 0);
          put("name", "Darryl");
          put("income", "180,000");
        }
      }),
      new RecordValue("department_id", new HashMap<String, Object>() {
        {
          put("staff_id", 1);
          put("department_id", 1);
          put("name", "Angela");
          put("income", "120,000");
        }
      }),
      new RecordValue("department_id", new HashMap<String, Object>() {
        {
          put("staff_id", 2);
          put("department_id", 2);
          put("name", "Jim");
          put("income", "100,000");
        }
      })
    ));
  }

}
