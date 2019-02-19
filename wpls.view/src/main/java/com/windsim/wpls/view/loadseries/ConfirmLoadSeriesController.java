package com.windsim.wpls.view.loadseries;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
class ConfirmLoadSeriesController implements Initializable, PopupContent {

  private Popup popup;

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @Autowired
  @Qualifier("simulatimeconfigHistorical")
  private Property<SimulationTimeConfig> simulationInptus;

  @FXML
  Button loadDataBtn;
  @FXML
  Button cancelBtn;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "weatherRecordsProperty")
  private final Property<WeatherRecords> weatherRecordsProperty = new SimpleObjectProperty<>();

  @Autowired
  @Qualifier(value = "confirmLoadSeriesProperty")
  private final Property<WeatherRecords> localWeatherRecordsProperty = new SimpleObjectProperty<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadDataBtn.setOnAction((evt) -> {
      this.popup.showProperty().setValue(false);
      this.weatherRecordsProperty.setValue(localWeatherRecordsProperty.getValue());
      this.localWeatherRecordsProperty.setValue(null);
    });
    cancelBtn.setOnAction((evt) -> {
      popup.showProperty().setValue(false);
    });
    this.localWeatherRecordsProperty.addListener((obs, old, change) -> {
      loadDataBtn.disableProperty().setValue(this.localWeatherRecordsProperty.getValue() == null);
    });
    loadDataBtn.disableProperty().setValue(this.localWeatherRecordsProperty.getValue() == null);
  }

  public void setRecordsLoader(WeatherRecordsLoader loader) {

    Model model = modelProperty.getValue();
    Set<String> stations = model.getWeatherStations().getStationIds();
    WeatherRecords weatherRecords = loader.loadWeatherRecords(stations, simulationInptus.getValue());
    this.localWeatherRecordsProperty.setValue(weatherRecords);
    
  }

  @Override
  public void setPopupWindow(Popup popup) {
    this.popup = popup;
    Platform.runLater(() -> {
      this.fxmlInitializer.addListener((i) -> {
        if (this.fxmlInitializer.getMainRoot().getScene() == null) {
          this.fxmlInitializer.getMainRoot().sceneProperty().addListener((s) -> {
            Scene scene = this.fxmlInitializer.getMainRoot().getScene();
            this.popup.windowProperty().bind(scene.windowProperty());
          });
        } else {
          Scene scene = this.fxmlInitializer.getMainRoot().getScene();
          this.popup.windowProperty().setValue(scene.getWindow());
          loadDataBtn.requestFocus();
        }
      });
    });
  }

  @Override
  public void onClose() throws IOException {

  }

}
