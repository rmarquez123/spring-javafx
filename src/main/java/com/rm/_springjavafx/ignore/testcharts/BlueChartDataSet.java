package com.rm._springjavafx.ignore.testcharts;

import com.rm.springjavafx.charts.SpringFxTimeSeries;
import com.rm.springjavafx.charts.TimeSeriesDataset;
import common.timeseries.TimeStepValue;
import common.timeseries.impl.DefaultTimeSeries;
import common.timeseries.impl.SimpleTimeStepValue;
import common.types.DateTimeRange;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
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
@TimeSeriesDataset(
  name = "Blue Data",
  chart = "testchart",
  dataset = 0,
  lineColorHex = "#483D8B"
)
public class BlueChartDataSet extends SpringFxTimeSeries implements InitializingBean{

  @Override
  public void afterPropertiesSet() throws Exception {
    TemporalAmount interval = Duration.ofHours(1); 
    List<TimeStepValue<Double>> records = new ArrayList<>();
    DateTimeRange dateTimeRange = DateTimeRange.of(ZoneId.of("UTC"), "yyyy/MM/dd HH:mm", 
      "2017/01/10 00:00", "2017/01/20 00:00");
    for (ZonedDateTime zonedDateTime : dateTimeRange.iterator(interval)) {
      records.add(new SimpleTimeStepValue<>(zonedDateTime, Math.random())); 
    }
    DefaultTimeSeries<Double> series = new DefaultTimeSeries<>(interval, records);
    super.valueAccessorProperty().setValue((tstepVal)->(Double) tstepVal.getUserObject());
    super.setTimeSeries(series);
  }
  
}
