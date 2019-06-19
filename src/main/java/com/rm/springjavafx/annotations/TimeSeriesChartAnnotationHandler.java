package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.charts.SpringFxTimeSeries;
import com.rm.springjavafx.charts.TimeSeriesChart;
import com.rm.springjavafx.charts.TimeSeriesChartPane;
import com.rm.springjavafx.charts.TimeSeriesDataset;
import java.util.Map;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class TimeSeriesChartAnnotationHandler implements InitializingBean, AnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }
  
  /**
   * 
   */
  @Override
  public void readyFxmls() {
    
  }

  /**
   * 
   * @param b
   * @return 
   */
  private String getChartId(Object b) {
    return b.getClass().getDeclaredAnnotation(TimeSeriesChart.class).id();
  }

  /**
   * 
   */
  @Override
  public void setNodes() {
    Map<String, Object> beans = this.appContext.getBeansWithAnnotation(TimeSeriesDataset.class);
    Map<String, Object> chartBeans = this.appContext.getBeansWithAnnotation(TimeSeriesChart.class);
    for (Object value : beans.values()) {
      if (value instanceof SpringFxTimeSeries) {
        SpringFxTimeSeries seriesDataset = (SpringFxTimeSeries) value; 
        TimeSeriesDataset annotation = value.getClass().getDeclaredAnnotation(TimeSeriesDataset.class);
        Object chartBean = chartBeans.values().stream()
          .filter((b)->getChartId(b).equals(annotation.chart()))
          .findFirst()
          .orElseThrow(()->new RuntimeException("No charts found with matching chart id : '" + annotation.chart() + "'")); 
        if (chartBean instanceof TimeSeriesChartPane) {
          XYPlot plot = ((TimeSeriesChartPane) chartBean).getPlot();
          TimeSeriesCollection collection = (TimeSeriesCollection) plot.getDataset(0);
          collection.addSeries(seriesDataset);
        }
      } else {
        throw new IllegalArgumentException("Bean with " + TimeSeriesDataset.class + " annotation should also extend '"
          + SpringFxTimeSeries.class + "'. Check args: " + value);
      }
    }
    System.out.println("blah blah");
  }
}
