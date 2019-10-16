package com.rm.springjavafx.charts;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.annotations.FxController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class TimeSeriesChartPane implements InitializingBean {

  @Autowired
  private FxmlInitializer _fxmlInitializer;
  private Pane chartPane;
  private final XYPlot plot;
  private final Property<List<String>> visibleDatasetsProperty = new SimpleObjectProperty();
  private final Property<List<String>> datasetsProperty = new SimpleObjectProperty();

  /**
   *
   */
  public TimeSeriesChartPane() {
    TimeSeriesChart chart = this.getClass().getDeclaredAnnotation(TimeSeriesChart.class);
    int datasets = chart.datasets();
    this.plot = new XYPlot();
    DateAxis dateAxis = new DateAxis();
    dateAxis.setAutoRange(true);
    this.plot.setDomainAxis(dateAxis);
    NumberAxis numberAxis = new NumberAxis();
    numberAxis.setAutoRangeIncludesZero(false);
    numberAxis.setAutoRangeStickyZero(false);
    numberAxis.setLabel(this.getLabel(0));
    numberAxis.setAutoRange(true);
    numberAxis.setAutoRangeIncludesZero(false);
    numberAxis.setAutoRangeStickyZero(false);
    this.plot.setRangeAxes(new ValueAxis[]{numberAxis});
    
    StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator();
    DefaultXYItemRenderer renderer = new DefaultXYItemRenderer();
    this.plot.setRenderer(renderer);

    for (int i = 0; i < datasets; i++) {
      this.plot.setDataset(i, new TimeSeriesCollection());
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
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this._fxmlInitializer.addListener((i) -> {
      TimeSeriesChart chart = this.getClass().getDeclaredAnnotation(TimeSeriesChart.class);
      FxController controller = this.getClass().getDeclaredAnnotation(FxController.class);
      String fxml = controller.fxml();
      Parent root = i.getRoot(fxml);
      this.chartPane = SpringFxUtils.getChildByID(root, chart.node());
      ChartViewer viewer = this.getChart();
      this.chartPane.getChildren().clear();
      SpringFxUtils.setNodeOnRefPane(this.chartPane, viewer);
    });
    this.postInit();
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
   */
  protected abstract void postInit();

  /**
   *
   * @return
   */
  public XYPlot getPlot() {
    return this.plot;
  }

  /**
   *
   * @param string
   * @param visible
   */
  public void setVisible(String string, boolean visible) {
    if (visible) {
      List<String> current = this.visibleDatasetsProperty.getValue();
      if (current == null) {
        this.visibleDatasetsProperty.setValue(Arrays.asList(string));
      } else if (!current.contains(string)) {
        List<String> newList = new ArrayList<>(current);
        newList.add(string);
        this.visibleDatasetsProperty.setValue(newList);
      }
    } else {
      List<String> current = this.visibleDatasetsProperty.getValue();
      if (current == null) {
        this.visibleDatasetsProperty.setValue(Collections.EMPTY_LIST);
      } else if (current.contains(string)) {
        List<String> newList = new ArrayList<>(current);
        newList.remove(string);
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
        for (String string : strings) {
          if (!current.contains(string)) {
            newList.add(string);
          }
        }
        this.visibleDatasetsProperty.setValue(newList);
      }
    } else {
      if (current == null) {
        this.visibleDatasetsProperty.setValue(Collections.EMPTY_LIST);
      } else {
        List<String> newList = new ArrayList<>(current);
        for (String string : strings) {
          if (current.contains(string)) {
            newList.remove(string);
          }
        }
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
}
