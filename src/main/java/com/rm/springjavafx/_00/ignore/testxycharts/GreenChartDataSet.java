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
  name = "Green Data",
  chart = "testchart",
  dataset = 1,
  lineColorHex = "#00aa00"
)
public class GreenChartDataSet extends SpringFxXYDataSet implements InitializingBean {

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    List<XYValue> records = new ArrayList<>();
    for (int i = 0; i < 100; i = i + 2) {
      double x = i;
      double y = Math.random() * 100;
      records.add(new XYValue(x, y, null));
    }
    records.sort((i1, i2) -> Double.compare(i1.getX().doubleValue(), i2.getX().doubleValue()));
    super.setTimeSeries(records);
  }

}
