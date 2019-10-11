package com.rm.springjavafx._00.ignore.testxycharts;

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
  name = "Blue Data",
  chart = "testchart",
  dataset = 0,
  lineColorHex = "#483D8B"
)
public class BlueChartDataSet extends SpringFxXYDataSet implements InitializingBean {
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    List<XYValue> records = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      double x = Math.random();
      double y = Math.random();
      records.add(new XYValue(x, y, null));
    }
    super.setTimeSeries(records);
  }

}
