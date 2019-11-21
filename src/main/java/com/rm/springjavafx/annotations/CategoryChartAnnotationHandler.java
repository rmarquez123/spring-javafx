package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.charts.category.CategoryChart;
import com.rm.springjavafx.charts.category.CategoryChartPane;
import com.rm.springjavafx.charts.category.CategoryCollectionManager;
import com.rm.springjavafx.charts.category.CategoryFxDataSet;
import com.rm.springjavafx.charts.category.SpringFxCategoryDataSet;
import common.bindings.RmBindings;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.Parent;
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
public class CategoryChartAnnotationHandler implements InitializingBean, AnnotationHandler {

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
    Map<String, Object> charts = this.appContext.getBeansWithAnnotation(CategoryChart.class);
    for (Map.Entry<String, Object> entry : charts.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof CategoryChartPane)) {
        throw new IllegalStateException(
          String.format("Bean '%s' does not implement '%s'", beanId, CategoryChartPane.class));
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
    Map<String, Object> charts = this.appContext.getBeansWithAnnotation(CategoryChart.class);
    for (Map.Entry<String, Object> entry : charts.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof CategoryChartPane)) {
        throw new IllegalStateException(
          String.format("Bean '%s' does not implement '%s'", beanId, CategoryChartPane.class));
      }
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      if (fxController == null) {
        throw new IllegalStateException(
          String.format("Bean '%s' is not annotated with '%s'", beanId, FxController.class));
      }
      String fxml = fxController.fxml();
      String nodeId = bean.getClass().getDeclaredAnnotation(CategoryChart.class).node();
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
    Map<String, Object> datasetBeans = this.appContext.getBeansWithAnnotation(CategoryFxDataSet.class);
    Map<String, Object> chartBeans = this.appContext.getBeansWithAnnotation(CategoryChart.class);
    Map<String, CategoryCollectionManager> managers = new HashMap<>();
    for (Map.Entry<String, Object> entry : datasetBeans.entrySet()) {
      Object value = entry.getValue();
      String beanId = entry.getKey();
      if (value instanceof SpringFxCategoryDataSet) {
        SpringFxCategoryDataSet seriesDataset = (SpringFxCategoryDataSet) value;
        try {
          seriesDataset.validate();
        } catch (Exception ex) {
          throw new RuntimeException(
            String.format("timeseries dataset '%s' is invalid", beanId), ex);
        }

        CategoryFxDataSet annotation = value.getClass().getDeclaredAnnotation(CategoryFxDataSet.class);
        Object chartBean = chartBeans.values().stream()
          .filter((b) -> getChartId(b).equals(annotation.chart()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("No charts found with matching chart id : '" + annotation.chart() + "'"));
        if (chartBean instanceof CategoryChartPane) {

          if (!managers.containsKey(annotation.chart())) {
            CategoryChartPane chart = (CategoryChartPane) chartBean;

            CategoryCollectionManager manager = new CategoryCollectionManager(chart);
            managers.put(annotation.chart(), manager);
          }
          CategoryCollectionManager manager = managers.get(annotation.chart());
          ObservableSet<String> reference = ((CategoryChartPane) chartBean).categoriesProperty();
          RmBindings.bindCollections(seriesDataset.categoriesProperty(), reference);
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
            String.format("Bean '%s' does not implement '%s'", value, CategoryChartPane.class));
        }
      } else {
        throw new IllegalArgumentException("Bean with " + CategoryFxDataSet.class + " annotation should also extend '"
          + SpringFxCategoryDataSet.class + "'. Check args: " + value);
      }
    }
  }

  /**
   *
   * @param b
   * @return
   */
  private String getChartId(Object b) {
    return b.getClass().getDeclaredAnnotation(CategoryChart.class).id();
  }

  /**
   *
   * @param bean
   * @return
   */
  private Node createChart(Object bean) {
    ChartViewer chartView = new ChartViewer();
    RmBindings.bindActionOnAnyChange(()->{
      Parent p = chartView.getParent();
      Platform.runLater(()->{
        System.out.println("bean = " + bean);
      });
    }, chartView.parentProperty());
    return chartView;
  }
}
