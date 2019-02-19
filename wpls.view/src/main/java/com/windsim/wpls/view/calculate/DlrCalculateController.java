package com.windsim.wpls.view.calculate;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import gov.inl.glass3.linesolver.DlrController;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.weather.WeatherRecords;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class DlrCalculateController implements Initializable, PopupContent {

  private Popup popup;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @Autowired
  @Qualifier(value = "weatherRecordsProperty")
  private Property<WeatherRecords> weatherRecordsProperty;

  @Autowired
  @Qualifier(value = "modelPointAmapacitiesProperty")
  private Property<ModelPointAmpacities> modelPointAmapacitiesProperty;
  @Autowired
  @Qualifier("simulatimeconfigHistorical")
  private Property<SimulationTimeConfig> simulationInptsProperty;
  @FXML
  Button runDlrCalcBtn;
  @FXML
  Button cancelBtn;
  @FXML
  ProgressBar progressBar;

  DlrController dlrConroller = new DlrController();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.progressBar.progressProperty().bind(this.dlrConroller.progressProperty());
    this.runDlrCalcBtn.disableProperty().bind(this.dlrConroller.calculatingProperty());
    this.cancelBtn.disableProperty().bind(Bindings.not(this.dlrConroller.calculatingProperty()));
    this.runDlrCalcBtn.setOnAction((evt)->{
      this.calculate();
    });
    
    this.cancelBtn.setOnAction((evt)->{
      this.dlrConroller.cancel();
    });
  }

  public void calculate() {
    new Thread(() -> {
      Model model = modelProperty.getValue();
      WeatherRecords weatherRecords = this.weatherRecordsProperty.getValue();
      SimulationTimeConfig simulationInputs = simulationInptsProperty.getValue();
      ModelPointAmpacities modelPointAmpacities = dlrConroller
        .computeSteadyStateAmpacity(model, simulationInputs, weatherRecords);
      Platform.runLater(() -> {
        this.modelPointAmapacitiesProperty.setValue(modelPointAmpacities);
        this.popup.showProperty().setValue(false);
      });
    }, "DLR-Calcuations").start();

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
          runDlrCalcBtn.requestFocus();
        }
      });
    });

  }

  @Override
  public void onClose() throws IOException {
  }

}
