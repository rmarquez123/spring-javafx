package com.windsim.wpls.view;

import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.RadarChart;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Ricardo Marquez
 */
public class TilesFxTest extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    List<ChartData> data = new ArrayList<>();
    ChartData sector1 = new ChartData("My chart", 10); 
    data.add(sector1); 
    ChartData sector2 = new ChartData("My chart", 30); 
    data.add(sector2); 
    ChartData sector3 = new ChartData("My chart", 55); 
    data.add(sector3); 
    ChartData sector4 = new ChartData("My chart", 70); 
    sector4.setStrokeColor(Color.RED);
    sector4.setTextColor(Color.RED);
    data.add(sector4); 
    RadarChart chart = new RadarChart(data); 
    
    Scene scene = new Scene(new VBox(chart));
    
    VBox.setVgrow(chart, Priority.ALWAYS);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
