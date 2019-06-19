package com.rm.springjavafx.charts;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.annotations.FxController;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.fx.ChartViewer;
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
  
  /**
   * 
   */
  public TimeSeriesChartPane() {
    TimeSeriesChart chart = this.getClass().getDeclaredAnnotation(TimeSeriesChart.class);
    int datasets = chart.datasets();
    this.plot = new XYPlot();
    this.plot.setDomainAxis(new DateAxis());
    this.plot.setRangeAxes(new ValueAxis[]{new NumberAxis()});
    for (int i = 0; i < datasets; i++) {
      plot.setDataset(i, new TimeSeriesCollection());
      plot.setRenderer(i, new DefaultXYItemRenderer());
    }
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
    JFreeChart jfreeChart = new JFreeChart(plot);
    ChartViewer result = new ChartViewer(jfreeChart);
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

}
