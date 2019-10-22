package com.rm._springjavafx.ignore.testxycharts;

import com.rm.springjavafx.charts.xy.SpringFxXYDataSet;
import com.rm.springjavafx.charts.xy.XYDataSet;
import com.rm.springjavafx.charts.xy.XYValue;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@XYDataSet(
  name = "Red Data",
  chart = "testchart",
  dataset = 2, 
  lineColorHex = "#8B0000"
)
public class RedChartDataSet extends SpringFxXYDataSet implements InitializingBean {

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    List<XYValue> records = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      
      double x = Math.random()*100;
      double y = Math.random()*100;
      records.add(new XYValue(x, y, null));
    }
    
    super.setTimeSeries(records);
  }
  
}
