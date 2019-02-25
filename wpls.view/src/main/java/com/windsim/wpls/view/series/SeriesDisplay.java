package com.windsim.wpls.view.series;

import com.rm.springjavafx.FxmlInitializer;
import com.windsim.wpls.view.DateTimeAxis;
import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherRecords;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.DoubleStream;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class SeriesDisplay implements InitializingBean {

  @Autowired
  private FxmlInitializer initializer;

  @Autowired
  @Qualifier(value = "selectedModelPoint")
  private Property<ModelPoint> modelPointProperty;

  @Autowired
  @Qualifier(value = "selectedSeriesType")
  private Property<SeriesType> seriesTypeProperty;

  @Autowired
  @Qualifier(value = "weatherRecordsProperty")
  private Property<WeatherRecords> weatherRecordsProperty;

  @Autowired
  @Qualifier(value = "modelPointAmapacitiesProperty")
  private Property<ModelPointAmpacities> modelPointAmapacitiesProperty;

  @Autowired
  @Qualifier(value = "frcstModelPointAmpacities")
  private Property<ModelPointAmpacities> frcstModelPointAmpacitiesProperty;

  @Autowired
  @Qualifier(value = "referenceDate")
  private Property<ZonedDateTime> referenceDateProperty;

  @Autowired
  @Qualifier(value = "zoneId")
  private Property<ZoneId> zoneIdProperty;

  /**
   *
   */
  private LineChart<Long, Double> seriesChart;
  private XYChart.Series<Long, Double> series;
  private XYChart.Series<Long, Double> refLine;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.initializer.addListener((i) -> {
      try {
        this.seriesChart = (LineChart<Long, Double>) this.initializer.getNode("fxml/SeriesViewPane.fxml", "seriesChart");
        this.seriesChart.animatedProperty().setValue(false);
        DateTimeAxis xAxis = (DateTimeAxis) this.initializer.getNode("fxml/SeriesViewPane.fxml", "xAxis");
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(true);

        NumberAxis ssfae;
        xAxis.autoRangingProperty().getValue();
        xAxis.tickLabelFormatterProperty().setValue(new StringConverter<Number>() {
          private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

          @Override
          public String toString(Number object) {
            LocalDateTime localDate = LocalDateTime.ofEpochSecond(object.longValue(), 0, ZoneOffset.ofHours(-8));
            String result = formatter.format(localDate);
            return result;
          }

          @Override
          public Number fromString(String string) {
            throw new UnsupportedOperationException("");
          }
        });

      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }

      this.series = new XYChart.Series<>();
      this.refLine = new XYChart.Series<>();
      ObservableList<XYChart.Series<Long, Double>> list = FXCollections
        .observableArrayList(this.series, this.refLine);
      this.seriesChart.dataProperty().setValue(list);

      this.seriesTypeProperty.addListener((obs, old, change) -> {
        this.updateSeriesDisplay();
      });

      this.modelPointProperty.addListener((obs, old, change) -> {
        this.updateSeriesDisplay();
      });

      this.weatherRecordsProperty.addListener((obs, old, change) -> {
        this.updateSeriesDisplay();
      });

      this.modelPointAmapacitiesProperty.addListener((obs, old, change) -> {
        this.updateSeriesDisplay();
      });

      this.frcstModelPointAmpacitiesProperty.addListener((obs, old, change) -> {
        this.updateSeriesDisplay();
      });

      this.referenceDateProperty.addListener((obs, old, change) -> {
        this.updateRefSeries();
      });
    });
  }

  /**
   *
   */
  private void updateRefSeries() {
    ZonedDateTime change = this.referenceDateProperty.getValue();
    if (change != null) {
      this.seriesChart.dataProperty().getValue().remove(this.refLine);
      ObservableList<XYChart.Data<Long, Double>> otherSeries = this.series.dataProperty().getValue();
      if (otherSeries != null && !otherSeries.isEmpty()) {
        Double upperBound = getUpperBound(otherSeries);
        Double lowerBound = getLowerBound(otherSeries);
        XYChart.Data<Long, Double> top = new XYChart.Data<>(change.toEpochSecond(), upperBound);
        XYChart.Data<Long, Double> bottom = new XYChart.Data<>(change.toEpochSecond(), lowerBound);
        this.refLine.setData(FXCollections.observableArrayList(top, bottom));
        this.refLine.getNode().setManaged(true);
        this.seriesChart.dataProperty().getValue().add(this.refLine);
      }
    }
  }

  private Double getLowerBound(ObservableList<XYChart.Data<Long, Double>> otherSeries) {
    DoubleStream mapToDouble = otherSeries.stream().mapToDouble((a) -> a.getYValue());
    Double lowerBound = mapToDouble.min().getAsDouble();
    return lowerBound;
  }

  private Double getUpperBound(ObservableList<XYChart.Data<Long, Double>> otherSeries) {
    DoubleStream mapToDouble = otherSeries.stream().mapToDouble((a) -> a.getYValue());
    Double upperBound = mapToDouble.max().getAsDouble();
    return upperBound;
  }

  /**
   *
   */
  private void updateSeriesDisplay() {
    if (Platform.isFxApplicationThread()) {
      updateSeriesDisplayImpl();
    } else {
      Platform.runLater(() -> {
        updateSeriesDisplayImpl();
      });
    }
  }

  private void updateSeriesDisplayImpl() throws RuntimeException {
    ObservableList<XYChart.Data<Long, Double>> result = this.series.getData();
    this.seriesChart.dataProperty().getValue().remove(this.series);
    this.series.dataProperty().getValue().clear();
    this.series.getNode().setStyle("chart-series-line");
    ModelPoint modelPoint = this.modelPointProperty.getValue();
    
    if (modelPoint != null) {
      SeriesType seriesType = this.seriesTypeProperty.getValue();
      if ((seriesType != null)) {
        SeriesType value = seriesType;
        ModelPointAmpacities modelPointAmpacities = modelPointAmapacitiesProperty.getValue();
        ModelPointAmpacities fcstedModelPointAmpacities = frcstModelPointAmpacitiesProperty.getValue();
        WeatherRecords weatherRecords = this.weatherRecordsProperty.getValue();
        ModelSeriesData seriesData = new ModelSeriesData(weatherRecords,
          modelPointAmpacities, fcstedModelPointAmpacities);
        value.displayRecords(seriesData, modelPoint, result);
      }
      String modelPointId = modelPoint.getModelPointId();
      if (seriesType != null) {
        String seriesTypeName = seriesType.name();
        this.seriesChart.getYAxis().labelProperty().set(seriesTypeName);
      }

      DateTimeAxis xAxis;
      try {
        xAxis = (DateTimeAxis) this.initializer.getNode("fxml/SeriesViewPane.fxml", "xAxis");
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex.getMessage(), ex);
      }
      Platform.runLater(() -> {
        for (XYChart.Data<Long, Double> data : result) {
          StringConverter<Number> converter = xAxis.tickLabelFormatterProperty().getValue();
          String dateString = converter.toString(data.getXValue());
          Tooltip t = new Tooltip(data.getYValue() + "\n" + dateString);
          t.setStyle("-fx-show-delay: 150ms");
          Tooltip.install(data.getNode(), t);
          data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (evt) -> {
            Instant instant = Instant.ofEpochSecond(data.getXValue());
            ZoneId zoneId = zoneIdProperty.getValue();
            ZonedDateTime d = ZonedDateTime.ofInstant(instant, zoneId);
            this.referenceDateProperty.setValue(d);
          });
        }
      });
      this.series.setName(modelPointId);
      this.seriesChart.getXAxis().labelProperty().set(null);
      this.seriesChart.dataProperty().getValue().add(this.series);
      this.updateRefSeries();
    }
  }

}
