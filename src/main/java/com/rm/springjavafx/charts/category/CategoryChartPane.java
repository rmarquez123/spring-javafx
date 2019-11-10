package com.rm.springjavafx.charts.category;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.annotations.FxController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class CategoryChartPane implements InitializingBean {

  @Autowired
  private FxmlInitializer _fxmlInitializer;

  @Autowired
  private ApplicationContext applicationContext;

  private Pane chartPane;
  private final CategoryPlot plot;
  private final Property<List<String>> visibleDatasetsProperty = new SimpleObjectProperty();
  private final Property<List<String>> datasetsProperty = new SimpleObjectProperty();
  private final CategoryFxDataSetGroup[] datasetgroups;
  private ObservableSet<String> categoriesProperty = null;

  /**
   *
   */
  public CategoryChartPane() {
    CategoryChart chart = this.getClass().getDeclaredAnnotation(CategoryChart.class);
    this.datasetgroups = chart.datasetgroups();
    this.plot = new CategoryPlot();
    this.plot.setOrientation(chart.orientation().toJFreeOrientation());
    CategoryAxis domainAxis = new CategoryAxis();
    this.plot.setDomainAxis(domainAxis);
    NumberAxis numberAxis = this.getRangeAxis();
    numberAxis.setAutoRange(true);
    numberAxis.setAutoRangeIncludesZero(false);
    numberAxis.setLabel(this.getLabel(0));
    this.plot.setRangeAxes(new ValueAxis[]{numberAxis});
    StandardCategoryToolTipGenerator ttg = new StandardCategoryToolTipGenerator();
    int i = -1;
    for (CategoryFxDataSetGroup dataset : datasetgroups) {
      i++;
      CategoryItemRenderer renderer = dataset.plotType().getRenderer();
      CategoryDataset ds = dataset.plotType().getDataset(dataset);
      this.plot.setDataset(i, ds);
      renderer.setSeriesToolTipGenerator(i, ttg);
      this.plot.setRenderer(i, renderer);
    }
    this.datasetsProperty.addListener((obs, old, change) -> {
      for (String string : change) {
        if (old != null && !old.contains(string)) {
          this.setVisible(string, true);
        }
      }
    });
  }

  /**
   *
   * @return
   */
  public final ReadOnlyProperty<List<String>> datasetsProperty() {
    return datasetsProperty;
  }

  /**
   *
   * @return
   */
  final Property<List<String>> writableDatasetsProperty() {
    return datasetsProperty;
  }

  /**
   *
   * @return
   */
  public final Property<List<String>> visibleDatasetsProperty() {
    return this.visibleDatasetsProperty;
  }

  /**
   *
   * @return
   */
  public CategoryFxDataSetGroup[] getDatasetgroups() {
    return datasetgroups;
  }

  /**
   *
   * @return
   */
  public CategoryFxDataSetGroup getDatasetgroup(int datasetId) {
    return datasetgroups[datasetId];
  }

  /**
   *
   * @throws Exception
   */
  @Override
  public final void afterPropertiesSet() throws Exception {
    CategoryChart chart = this.getClass().getDeclaredAnnotation(CategoryChart.class);
    this.categoriesProperty = (ObservableSet<String>) this.applicationContext.getBean(chart.categories());
    this._fxmlInitializer.addListener((i) -> {
      FxController controller = this.getClass().getDeclaredAnnotation(FxController.class);
      String fxml = controller.fxml();
      Parent root = i.getRoot(fxml);
      this.chartPane = SpringFxUtils.getChildByID(root, chart.node());
      ChartViewer viewer = this.getChart();
      this.chartPane.getChildren().clear();
      SpringFxUtils.setNodeOnRefPane(this.chartPane, viewer);
      this.categoriesProperty.addListener((SetChangeListener.Change<? extends String> change) -> {
        this.updateDataSetCategories();
      });
      this.updateDataSetCategories();
    });
    this.postInit();
  }
  
  /**
   * 
   */
  private void updateDataSetCategories() {
    for (int j = 0; j < plot.getDatasetCount(); j++) {
      JFreeCategoryDataSet ds = (JFreeCategoryDataSet) plot.getDataset(j);
      ds.setCategories(categoriesProperty);
    }
    Platform.runLater(()->plot.getChart().fireChartChanged());
  }

  /**
   *
   * @return
   */
  private ChartViewer getChart() {
    JFreeChart jfreeChart = new JFreeChart(this.plot);
    ChartViewer result = new ChartViewer(jfreeChart, true);
    return result;
  }

  /**
   *
   * @return
   */
  public CategoryPlot getPlot() {
    return this.plot;
  }

  /**
   *
   * @param key
   * @param visible
   */
  public void setVisible(String key, boolean visible) {
    if (visible) {
      List<String> current = this.visibleDatasetsProperty.getValue();
      if (current == null) {
        this.visibleDatasetsProperty.setValue(Arrays.asList(key));
      } else if (!current.contains(key)) {
        List<String> newList = new ArrayList<>(current);
        newList.add(key);
        this.visibleDatasetsProperty.setValue(newList);
      }
    } else {
      List<String> current = this.visibleDatasetsProperty.getValue();
      if (current == null) {
        this.visibleDatasetsProperty.setValue(Collections.EMPTY_LIST);
      } else if (current.contains(key)) {
        List<String> newList = new ArrayList<>(current);
        newList.remove(key);
        this.visibleDatasetsProperty.setValue(newList);
      }
    }
  }

  /**
   *
   * @param string
   * @param visible
   */
  public void setVisible(List<String> strings, boolean visible) {
    List<String> current = this.visibleDatasetsProperty.getValue();
    if (visible) {
      if (current == null) {
        this.visibleDatasetsProperty.setValue(strings);
      } else {
        List<String> newList = new ArrayList<>(current);
        strings.stream()
          .filter((s) -> !current.contains(s))
          .forEach((s) -> newList.add(s));
        this.visibleDatasetsProperty.setValue(newList);
      }
    } else {
      if (current == null) {
        this.visibleDatasetsProperty.setValue(Collections.EMPTY_LIST);
      } else {
        List<String> newList = new ArrayList<>(current);
        newList.removeIf(e -> strings.contains(e));
        this.visibleDatasetsProperty.setValue(newList);
      }
    }
  }

  /**
   *
   * @param i
   * @return
   */
  protected String getLabel(int i) {
    return null;
  }

  /**
   *
   */
  protected abstract void postInit();

  /**
   *
   * @return
   */
  protected NumberAxis getRangeAxis() {
    return new NumberAxis();
  }

  public ObservableSet<String> categoriesProperty() {
    return categoriesProperty;
  }
}
