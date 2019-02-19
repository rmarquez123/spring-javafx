package com.windsim.wpls.view.series;

import com.rm.springjavafx.FxmlInitializer;
import gov.inl.glass3.linesolver.DlrController;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.linesolver.impl.files.FilesWeatherRecordsLoader;
import gov.inl.glass3.linesolver.impl.files.SeriesDirectory;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.File;
import java.time.ZoneId;
import java.util.Set;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class SeriesPaneController implements InitializingBean {

  @Autowired
  @Qualifier("zoneId")
  private Property<ZoneId> zoneIdProperty;
  @Autowired
  @Qualifier("simulatimeconfigHistorical")
  private Property<SimulationTimeConfig> simulationInputsProperty;

  @Autowired
  private FxmlInitializer initializer;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

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

  @Override
  public void afterPropertiesSet() throws Exception {
    this.initializer.addListener((FxmlInitializer i) -> {
//      initLoadAllTimeSeriesBtn();
//      initCalcAmapacitiesBtn();
      initComboBox();
    });
  }

  /**
   *
   */
  private void initLoadAllTimeSeriesBtn() {
    Button button;
    try {
      button = (Button) this.initializer.getNode("fxml/SeriesViewPane.fxml", "loadAllTimeSeriesBtn");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    button.disableProperty().setValue(modelProperty.getValue() == null);
    this.modelProperty.addListener((obs, old, change) -> {
      button.disableProperty().setValue(modelProperty.getValue() == null);
    });
    button.setOnAction((evt) -> {
      button.disableProperty().setValue(true);
      try {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(button.getScene().getWindow());
        if (dir != null) {
          Model model = modelProperty.getValue();
          Set<String> stations = model.getWeatherStations().getStationIds();
          SeriesDirectory seriesDirectory = new SeriesDirectory(dir);

          WeatherRecordsLoader loader = new FilesWeatherRecordsLoader(seriesDirectory, zoneIdProperty.getValue());
          WeatherRecords weatherRecords = loader.loadWeatherRecords(stations, simulationInputsProperty.getValue());

          this.weatherRecordsProperty.setValue(weatherRecords);
        }
      } finally {
        button.disableProperty().setValue(false);
      }
    });
  }

  /**
   *
   */
  public void initCalcAmapacitiesBtn() {
    Button button;
    try {
      button = (Button) this.initializer.getNode("fxml/SeriesViewPane.fxml", "calculateAmapacitiesBtn");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    button.disableProperty().setValue(modelProperty.getValue() == null);
    this.modelProperty.addListener((obs, old, change) -> {
      button.disableProperty().setValue(modelProperty.getValue() == null && weatherRecordsProperty.getValue() == null);
    });
    this.weatherRecordsProperty.addListener((obs, old, change) -> {
      button.disableProperty().setValue(modelProperty.getValue() == null && weatherRecordsProperty.getValue() == null);
    });
    button.setOnAction((evt) -> {
      button.disableProperty().setValue(true);
      try {
        Model model = modelProperty.getValue();
        WeatherRecords weatherRecords = this.weatherRecordsProperty.getValue();
        if (weatherRecords != null) {
          DlrController dlrConroller = new DlrController();
          ModelPointAmpacities modelPointAmpacities = dlrConroller.computeSteadyStateAmpacity(model, simulationInputsProperty.getValue(), weatherRecords);
          this.modelPointAmapacitiesProperty.setValue(modelPointAmpacities);
        } else {
          throw new IllegalStateException("Weather records have not been loaded yet");
        }
      } finally {
        button.disableProperty().setValue(false);
      }
    });
  }

  /**
   *
   * @throws RuntimeException
   */
  private void initComboBox() throws RuntimeException {
    ComboBox<SeriesType> seriesTypeSelectorCmbBox;
    try {
      seriesTypeSelectorCmbBox = (ComboBox) this.initializer.getNode("fxml/SeriesViewPane.fxml", "seriesTypeSelectorCmbBox");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    if (seriesTypeSelectorCmbBox.getItems().isEmpty()) {
      seriesTypeSelectorCmbBox.getItems().addAll(SeriesType.values());
    }
    seriesTypeSelectorCmbBox.getSelectionModel().selectedItemProperty().addListener((obs, old, change) -> {
      seriesTypeProperty.setValue(change);
    });
    seriesTypeProperty.addListener((obs, old, change) -> {
      seriesTypeSelectorCmbBox.getSelectionModel().select(change);
    });
    seriesTypeSelectorCmbBox.getSelectionModel().select(this.seriesTypeProperty.getValue());
  }
}
