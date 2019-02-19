package com.windsim.wpls.view.realtime;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RealTimeFcstOptionsController implements Initializable, InitializingBean, PopupContent {

  @Autowired
  FxmlInitializer fxmlInitializer;

  @FXML
  private Spinner<Integer> timeIntervalSpinner;

  @FXML
  private ComboBox<TemporalUnit> timeIntervalUnitsCmbBox;

  @FXML
  private Spinner<Integer> fcstTimeStepsSpinner;

  @FXML
  private Spinner<Integer> repeatStepsSpinner;

  @FXML
  private Button saveBtn;

  @FXML
  private Button cancelBtn;

  @Autowired
  @Qualifier("timeIntervalHistorical")
  private Property<TemporalAmount> timeIntervalProperty;

  @Autowired
  @Qualifier("fcstTimeSteps")
  private IntegerProperty fcstTimeStepsProperty;

  @Autowired
  @Qualifier("repeatFcstTimeSteps")
  private IntegerProperty repeatFcstTimeStepsProperty;

  private Popup popup;
  private Runnable onSave;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.initTimeIntervalUnitsCmbBox();
    this.initTimeIntervalSpinner();
    this.initFcstTimeStepsSpinner();
    this.initRepeatStepsBtn();
    
    this.cancelBtn.setOnAction((evt)->{
      this.popup.showProperty().setValue(false);
    });
    
    this.saveBtn.setOnAction((evt) -> {
      this.repeatFcstTimeStepsProperty.setValue(this.repeatStepsSpinner.getValue());
      this.fcstTimeStepsProperty.setValue(this.fcstTimeStepsSpinner.getValue());
      Integer amount = this.timeIntervalSpinner.valueProperty().getValue();
      TemporalUnit unit = this.timeIntervalUnitsCmbBox.valueProperty().getValue();
      if (amount != null && unit != null) {
        TemporalAmount timeInterval = Duration.of(amount.longValue(), unit);
        this.timeIntervalProperty.setValue(timeInterval);
      } else {
        this.timeIntervalProperty.setValue(null);
      }
      this.onSave.run();
      this.popup.showProperty().setValue(false);
    });
  }

  /**
   *
   * @param runnable
   */
  public void setOnSaveAction(Runnable runnable) {
    this.onSave = runnable;
  }

  /**
   *
   */
  private void initRepeatStepsBtn() {
    this.repeatStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60));
    this.repeatStepsSpinner.getValueFactory().setValue(1);
  }

  /**
   *
   */
  private void initFcstTimeStepsSpinner() {
    this.fcstTimeStepsSpinner.valueProperty().addListener((obs, old, change) -> {

    });
    this.fcstTimeStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60));
    this.fcstTimeStepsSpinner.getValueFactory().setValue(1);
  }

  /**
   *
   */
  private void initTimeIntervalSpinner() {
    this.timeIntervalSpinner.valueProperty().addListener((obs, old, change) -> {

    });
    this.timeIntervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60));
    this.timeIntervalSpinner.getValueFactory().setValue(1);
  }

  /**
   *
   */
  private void initTimeIntervalUnitsCmbBox() {
    this.timeIntervalUnitsCmbBox.valueProperty().addListener((obs, old, change) -> {
    });
    this.timeIntervalUnitsCmbBox.getItems().addAll(ChronoUnit.values());
    this.timeIntervalUnitsCmbBox.valueProperty().setValue(ChronoUnit.HOURS);
  }

  @Override
  public void afterPropertiesSet() throws Exception {

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
          this.fcstTimeStepsSpinner.requestFocus();
        }
      });
    });
  }

  @Override
  public void onClose() throws IOException {

  }

}
