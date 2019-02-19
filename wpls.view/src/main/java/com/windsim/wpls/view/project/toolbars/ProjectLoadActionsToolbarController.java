package com.windsim.wpls.view.project.toolbars;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.windsim.wpls.view.calculate.DlrCalculateController;
import com.windsim.wpls.view.loadseries.RequestLoadSeriesAction;
import com.windsim.wpls.view.projectopen.RequestProjectOpenAction;
import com.windsim.wpls.view.series.SeriesType;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.weather.WeatherRecords;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class ProjectLoadActionsToolbarController implements InitializingBean {

  @Autowired
  @Qualifier("simulatimeconfigHistorical")
  private Property<SimulationTimeConfig> simulationInptsProperty;

  @Autowired
  private FxmlInitializer initializer;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "selectedSeriesType")
  private Property<SeriesType> seriesTypeProperty;

  @Autowired
  @Qualifier(value = "weatherRecordsProperty")
  private Property<WeatherRecords> weatherRecordsProperty;
  
  @Autowired
  @Qualifier(value = "calculateDlrPopup")
  private Popup calculateDlrPopup;
  
  
  
  @Autowired
  DlrCalculateController calculateDlrPopupController;

  @Autowired
  @Qualifier("requestLoadSeriesAction")
  Property<RequestLoadSeriesAction> requestLoadSeriesActionProperty;
  @Autowired
  @Qualifier("requestProjectOpenAction")
  Property<RequestProjectOpenAction> requestProjectOpenActionProperty;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.initializer.addListener((FxmlInitializer i) -> {
      initOpenProjectBtn();
      initLoadAllTimeSeriesBtn();
      initCalcAmapacitiesBtn();
      initComboBox();
    });
  }

  /**
   *
   */
  private void initOpenProjectBtn() {
    Button button;
    try {
      button = (Button) this.initializer.getNode("fxml/Main.fxml", "openProjectBtn");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }

    button.visibleProperty().setValue(modelProperty.getValue() == null);
    button.managedProperty().setValue(modelProperty.getValue() == null);
    this.modelProperty.addListener((obs, old, change) -> {
      button.visibleProperty().setValue(modelProperty.getValue() == null);
      button.managedProperty().setValue(modelProperty.getValue() == null);
    });
    button.setOnAction((evt) -> {
      requestProjectOpenActionProperty.getValue().onAction((m) -> {
      });
    });
  }

  /**
   *
   */
  private void initLoadAllTimeSeriesBtn() {
    Button button;
    try {
      button = (Button) this.initializer.getNode("fxml/Main.fxml", "loadAllTimeSeriesBtn");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    button.disableProperty().setValue(this.isNotLoaderReady());
    this.modelProperty.addListener((obs, old, change) -> {
      button.disableProperty().setValue(this.isNotLoaderReady());
    });

    this.simulationInptsProperty.addListener((obs, old, change) -> {
      button.disableProperty().setValue(this.isNotLoaderReady());
    });
    button.setOnAction((evt) -> {
      button.disableProperty().setValue(true);
      try {
        this.requestLoadSeriesActionProperty.getValue().onAction();
      } finally {
        button.disableProperty().setValue(false);
      }
    });
  }

  /**
   *
   * @return
   */
  private boolean isNotLoaderReady() {
    return modelProperty.getValue() == null || this.simulationInptsProperty.getValue() == null;
  }

  /**
   *
   */
  public void initCalcAmapacitiesBtn() {
    Button button;
    try {
      button = (Button) this.initializer.getNode("fxml/Main.fxml", "calculateAmapacitiesBtn");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    button.disableProperty().setValue(modelProperty.getValue() == null);
    this.modelProperty.addListener((obs, old, change) -> {
      WeatherRecords weatherRecord = weatherRecordsProperty.getValue();
      button.disableProperty().setValue(modelProperty.getValue() == null && weatherRecord == null);
    });
    this.weatherRecordsProperty.addListener((obs, old, change) -> {
      WeatherRecords weatherRecord = weatherRecordsProperty.getValue();
      button.disableProperty().setValue(modelProperty.getValue() == null && weatherRecord == null);
    });
    button.setOnAction((evt) -> {
      button.disableProperty().setValue(true);
      try {
        Model model = modelProperty.getValue();
        WeatherRecords weatherRecords = this.weatherRecordsProperty.getValue();
        if (weatherRecords != null) {
          this.calculateDlrPopupController.calculate();
          this.calculateDlrPopup.show();

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
    seriesTypeSelectorCmbBox.getItems().addAll(SeriesType.values());
    seriesTypeSelectorCmbBox.getSelectionModel().selectedItemProperty().addListener((obs, old, change) -> {
      seriesTypeProperty.setValue(change);
    });
    seriesTypeProperty.addListener((obs, old, change) -> {
      seriesTypeSelectorCmbBox.getSelectionModel().select(change);
    });
    seriesTypeSelectorCmbBox.getSelectionModel().select(this.seriesTypeProperty.getValue());
  }
}
