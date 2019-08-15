package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.charts.xy.SpringFxXYDataSet;
import com.rm.springjavafx.charts.xy.XYChart;
import com.rm.springjavafx.charts.xy.XYChartPane;
import com.rm.springjavafx.charts.xy.XYCollectionManager;
import com.rm.springjavafx.charts.xy.XYDataSet;
import common.bindings.RmBindings;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jfree.chart.fx.ChartViewer;
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
public class XYChartAnnotationHandler implements InitializingBean, AnnotationHandler {

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
    Map<String, Object> charts = this.appContext.getBeansWithAnnotation(XYChart.class);
    for (Map.Entry<String, Object> entry : charts.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof XYChartPane)) {
        throw new IllegalStateException(
          String.format("Bean '%s' does not implement '%s'", beanId, XYChartPane.class));
      }
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      if (fxController == null) {
        throw new IllegalStateException(
          String.format("Bean '%s' is not annotated with '%s'", beanId, FxController.class));
      }
      String fxml = fxController.fxml();
      this.fxmlInitializer.addFxml(fxml);
    }
  }

  /**
   *
   */
  @Override
  public void setNodes() {
    this.initCharts();
    this.initDatasets();
  }

  private void initCharts() {
    Map<String, Object> charts = this.appContext.getBeansWithAnnotation(XYChart.class);
    for (Map.Entry<String, Object> entry : charts.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof XYChartPane)) {
        throw new IllegalStateException(
          String.format("Bean '%s' does not implement '%s'", beanId, XYChartPane.class));
      }
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      if (fxController == null) {
        throw new IllegalStateException(
          String.format("Bean '%s' is not annotated with '%s'", beanId, FxController.class));
      }
      String fxml = fxController.fxml();
      String nodeId = bean.getClass().getDeclaredAnnotation(XYChart.class).node();
      Pane refPane;
      try {
        refPane = (Pane) this.fxmlInitializer.getNode(fxml, nodeId);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
      if (refPane == null) {
        throw new IllegalStateException("Node does not exist.  Check args: {"
          + "fxml = " + fxml
          + ", nodeID = " + nodeId
          + "}");

      }
      Node node = this.createChart(bean);
      SpringFxUtils.setNodeOnRefPane(refPane, node);
    }
  }

  /**
   *
   */
  private void initDatasets() {
    Map<String, Object> datasetBeans = this.appContext.getBeansWithAnnotation(XYDataSet.class);
    Map<String, Object> chartBeans = this.appContext.getBeansWithAnnotation(XYChart.class);
    Map<String, XYCollectionManager> managers = new HashMap<>();
    for (Map.Entry<String, Object> entry : datasetBeans.entrySet()) {
      Object value = entry.getValue();
      String beanId = entry.getKey();
      if (value instanceof SpringFxXYDataSet) {
        SpringFxXYDataSet seriesDataset = (SpringFxXYDataSet) value;
        try {
          seriesDataset.validate();
        } catch (Exception ex) {
          throw new RuntimeException(
            String.format("timeseries dataset '%s' is invalid", beanId), ex);
        }
        XYDataSet annotation = value.getClass().getDeclaredAnnotation(XYDataSet.class);
        Object chartBean = chartBeans.values().stream()
          .filter((b) -> getChartId(b).equals(annotation.chart()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("No charts found with matching chart id : '" + annotation.chart() + "'"));
        if (chartBean instanceof XYChartPane) {
          if (!managers.containsKey(annotation.chart())) {
            XYChartPane timeSeriesChart = (XYChartPane) chartBean;
            XYCollectionManager manager = new XYCollectionManager(timeSeriesChart);
            managers.put(annotation.chart(), manager);
          }
          XYCollectionManager manager = managers.get(annotation.chart());
          manager.addDataSet(seriesDataset);
          String visibilityBean = annotation.visibilityProperty();
          if (!visibilityBean.isEmpty()) {
            Property<Boolean> visibilityProperty = (Property<Boolean>) this.appContext.getBean(visibilityBean);
            if (visibilityBean == null) {
              throw new IllegalStateException(
                String.format("Bean does not exist for bean id = '%s'", beanId));
            }
            RmBindings.bindActionOnAnyChange(() -> {
              Boolean visible = visibilityProperty.getValue();
              if (visible != null) {
                manager.getChart().setVisible(annotation.name(), visible);
              }
            }, visibilityProperty);
            Boolean visible = visibilityProperty.getValue();
            if (visible != null) {
              manager.getChart().setVisible(annotation.name(), visible);
            }
          }
        } else {
          throw new IllegalStateException(
            String.format("Bean '%s' does not implement '%s'", value, XYChart.class));
        }
      } else {
        throw new IllegalArgumentException("Bean with " + XYDataSet.class + " annotation should also extend '"
          + SpringFxXYDataSet.class + "'. Check args: " + value);
      }
    }
  }

  /**
   *
   * @param b
   * @return
   */
  private String getChartId(Object b) {
    return b.getClass().getDeclaredAnnotation(XYChart.class).id();
  }
  
  /**
   * 
   * @param bean
   * @return 
   */
  private Node createChart(Object bean) {
    ChartViewer chartView = new ChartViewer();
    return chartView;
  }
}
