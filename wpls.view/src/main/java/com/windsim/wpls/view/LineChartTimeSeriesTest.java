package com.windsim.wpls.view;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author Ricardo Marquez
 */
public class LineChartTimeSeriesTest extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Duration duration = Duration.of(1, ChronoUnit.HOURS);
    DateTimeAxis xAxis = new DateTimeAxis(duration);
    xAxis.setAutoRanging(true);

    xAxis.setTickLabelFormatter(new StringConverter<java.lang.Number>() {
      private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

      @Override
      public String toString(Number object) {
        ZoneOffset.getAvailableZoneIds();
        LocalDateTime localDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(object.longValue()), ZoneId.of("US/Pacific"));
        String result = formatter.format(localDate);
        return result;
      }

      @Override
      public Number fromString(String string) {
        throw new UnsupportedOperationException("");
      }
    });
    NumberAxis yAxis = new NumberAxis();
    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setAnimated(false);
    Map<ZonedDateTime, Double> timeSeries = new LinkedHashMap<>();
    ZonedDateTime startDt = ZonedDateTime.parse("2018-01-01T00:00:00+01:00", DateTimeFormatter.ISO_DATE_TIME);
    timeSeries.put(startDt, Math.random());
    for (int i = 1; i < 20; i++) {
      ZonedDateTime dateTime = startDt.plus(duration.multipliedBy(i));
      double value = Math.random();
      timeSeries.put(dateTime, value);
    }
    ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
    for (Map.Entry<ZonedDateTime, Double> entry : timeSeries.entrySet()) {
      long xValue = entry.getKey().toEpochSecond();
      double yValue = entry.getValue();
      XYChart.Data<Number, Number> chartDataPoint = new XYChart.Data<>(xValue, yValue);
      
      data.add(chartDataPoint);
    }
    Property<ZonedDateTime> selectedDateProperty = new SimpleObjectProperty<>();
    selectedDateProperty.addListener((obs, old, change)->{
      System.out.println(String.valueOf(selectedDateProperty.getValue()));
    });
    Platform.runLater(() -> {
      for (XYChart.Data<Number, Number> data1 : data) {
        String x = xAxis.tickLabelFormatterProperty().getValue().toString(data1.getXValue());        
        Tooltip tooltip = new Tooltip(String.valueOf(data1.getYValue() + "\n" + x));
        tooltip.setStyle("-fx-show-delay: 50ms");
        Node node = data1.getNode();
        Tooltip.install(node, tooltip);
        node.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
          ZoneId zoneId = ZoneId.of("US/Pacific");
          Instant ofEpochSecond = Instant.ofEpochSecond(data1.getXValue().longValue());
          ZonedDateTime ofInstant = ZonedDateTime.ofInstant(ofEpochSecond, zoneId);
          selectedDateProperty.setValue(ofInstant);
        });
      }
    });
    XYChart.Series<Number, Number> series = new XYChart.Series<>(data);
    series.setName("Random Values");
    XYChart.Series<Number, Number> referenceSeries = new XYChart.Series<>();
    referenceSeries.setName("Reference Line");
    selectedDateProperty.addListener((obs, old, change)->{
      Double upperBound = ((ValueAxis) chart.getYAxis()).getUpperBound();
      Double lowerBound = ((ValueAxis) chart.getYAxis()).getLowerBound();
      XYChart.Data<Number, Number> top = new XYChart.Data<>(change.toEpochSecond(), upperBound.longValue());      
      XYChart.Data<Number, Number> bottom = new XYChart.Data<>(change.toEpochSecond(), lowerBound.longValue());      
      referenceSeries.setData(FXCollections.observableArrayList(top, bottom));
      referenceSeries.getNode().setManaged(false);
    });
    chart.dataProperty().setValue(FXCollections.observableArrayList(series, referenceSeries));
    HBox propertiesBar = new HBox();
    propertiesBar.setSpacing(10.0);
    HBox.setMargin(propertiesBar, new Insets(10));
    Label tickLabelGap = new Label();
    tickLabelGap.textProperty().bind(xAxis.tickLabelGapProperty().asString());
    propertiesBar.getChildren().add(new HBox(new Label("tick label gap: "), tickLabelGap));

    Label tickLengthGap = new Label();
    tickLengthGap.textProperty().bind(xAxis.tickLengthProperty().asString());
    propertiesBar.getChildren().add(new HBox(new Label("tick length gap: "), tickLengthGap));

    Label tickUnit = new Label();
    tickUnit.textProperty().bind(xAxis.tickUnitProperty().asString());
    propertiesBar.getChildren().add(new HBox(new Label("tick unit: "), tickUnit));

    xAxis.tickUnitProperty().addListener((obs, old, change) -> {
      Platform.runLater(() -> {
        xAxis.tickUnitProperty().setValue(600);
      });
    });

    Scene scene = new Scene(new VBox(propertiesBar, chart));
    chart.getStylesheets().add("styles/chart-style.css");
    VBox.setVgrow(propertiesBar, Priority.NEVER);
    VBox.setVgrow(chart, Priority.ALWAYS);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
