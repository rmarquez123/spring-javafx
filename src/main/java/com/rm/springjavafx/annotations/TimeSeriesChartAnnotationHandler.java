package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.charts.timeseries.SpringFxTimeSeries;
import com.rm.springjavafx.charts.timeseries.TimeSeriesChart;
import com.rm.springjavafx.charts.timeseries.TimeSeriesChartPane;
import com.rm.springjavafx.charts.timeseries.TimeSeriesCollectionManager;
import com.rm.springjavafx.charts.timeseries.TimeSeriesDataset;
import common.bindings.RmBindings;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.springframework.beans.BeansException;
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
    Map<String, Object> charts = this.appContext.getBeansWithAnnotation(TimeSeriesChart.class);
    for (Map.Entry<String, Object> entry : charts.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof TimeSeriesChartPane)) {
        throw new IllegalStateException(
          String.format("Bean '%s' does not implement '%s'", beanId, TimeSeriesChartPane.class));
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
    Map<String, Object> charts = this.appContext.getBeansWithAnnotation(TimeSeriesChart.class);
    for (Map.Entry<String, Object> entry : charts.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof TimeSeriesChartPane)) {
        throw new IllegalStateException(
          String.format("Bean '%s' does not implement '%s'", beanId, TimeSeriesChartPane.class));
      }
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      if (fxController == null) {
        throw new IllegalStateException(
          String.format("Bean '%s' is not annotated with '%s'", beanId, FxController.class));
      }
      String fxml = fxController.fxml();
      String nodeId = bean.getClass().getDeclaredAnnotation(TimeSeriesChart.class).node();
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
      this.initClickHandler(bean);
    }
  }

  /**
   *
   * @param bean
   * @throws BeansException
   */
  private void initClickHandler(Object bean) throws BeansException {
    String clickId = bean.getClass().getDeclaredAnnotation(TimeSeriesChart.class).clickId();
    Consumer<Object> clicker;
    if (!clickId.isEmpty()
      && (clicker = (Consumer<Object>) this.appContext.getBean(clickId)) != null) {
      ((TimeSeriesChartPane) bean).viewerProperty().addListener((obs, old, change) -> {
        this.addChartMouseListener(change, clicker);
      });
      ChartViewer viewer = ((TimeSeriesChartPane) bean).viewerProperty().getValue();
      if (viewer != null) {
        this.addChartMouseListener(viewer, clicker);
      }
    }
  }

  private void addChartMouseListener(ChartViewer change, Consumer<Object> clicker) {
    change.addChartMouseListener(new ChartMouseListenerFX() {
      @Override
      public void chartMouseClicked(ChartMouseEventFX evt) {
        if (evt.getEntity() instanceof XYItemEntity) {
          XYItemEntity entity = (XYItemEntity) evt.getEntity();
          clicker.accept(entity);
        }
      }

      @Override
      public void chartMouseMoved(ChartMouseEventFX event) {
      }
    });
  }

  /**
   *
   */
  private void initDatasets() {
    Map<String, Object> datasetBeans = this.appContext.getBeansWithAnnotation(TimeSeriesDataset.class);
    Map<String, Object> chartBeans = this.appContext.getBeansWithAnnotation(TimeSeriesChart.class);
    Map<String, TimeSeriesCollectionManager> managers = new HashMap<>();
    for (Map.Entry<String, Object> entry : datasetBeans.entrySet()) {
      Object value = entry.getValue();
      String beanId = entry.getKey();
      if (value instanceof SpringFxTimeSeries) {
        SpringFxTimeSeries seriesDataset = (SpringFxTimeSeries) value;
        try {
          seriesDataset.validate();
        } catch (Exception ex) {
          throw new RuntimeException(
            String.format("timeseries dataset '%s' is invalid", beanId), ex);
        }

        TimeSeriesDataset annotation = ((SpringFxTimeSeries) value).getConfiguration();
        Object chartBean = chartBeans.values().stream()
          .filter((b) -> getChartId(b).equals(annotation.chart()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("No charts found with matching chart id : '" + annotation.chart() + "'"));
        if (chartBean instanceof TimeSeriesChartPane) {
          if (!managers.containsKey(annotation.chart())) {
            TimeSeriesChartPane timeSeriesChart = (TimeSeriesChartPane) chartBean;
            TimeSeriesCollectionManager manager = new TimeSeriesCollectionManager(timeSeriesChart);
            managers.put(annotation.chart(), manager);
            timeSeriesChart.setManager(manager);
          }
          TimeSeriesCollectionManager manager = managers.get(annotation.chart());
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
            String.format("Bean '%s' does not implement '%s'", value, TimeSeriesChart.class));
        }
      } else {
        throw new IllegalArgumentException("Bean with " + TimeSeriesDataset.class + " annotation should also extend '"
          + SpringFxTimeSeries.class + "'. Check args: " + value);
      }
    }
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
   * @param bean
   * @return
   */
  private Node createChart(Object bean) {
    ChartViewer chartView = new ChartViewer();
    return chartView;
  }
}
