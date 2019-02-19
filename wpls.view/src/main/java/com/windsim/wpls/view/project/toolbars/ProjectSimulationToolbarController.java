package com.windsim.wpls.view.project.toolbars;

import com.rm.springjavafx.components.ChildNodeWrapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
public class ProjectSimulationToolbarController implements InitializingBean {

  @Autowired
  @Qualifier("startDtDatePicker")
  ChildNodeWrapper<DatePicker> startDtDatePicker;

  @Autowired
  @Qualifier("endDtDatePicker")
  ChildNodeWrapper<DatePicker> endDtDatePicker;

  @Autowired
  @Qualifier("zondIdComboBox")
  private ChildNodeWrapper<ComboBox> zoneIdComboBox;
  @Autowired
  @Qualifier("timeCountSpinner")
  private ChildNodeWrapper<Spinner<Integer>> timeCountSpinner;
  @Autowired
  @Qualifier("timeUnitCmbBox")
  private ChildNodeWrapper<ComboBox<TemporalUnit>> timeUnitCmbBox;

  @Autowired
  @Qualifier("zoneId")
  private Property<ZoneId> zoneIdProperty;

  @Autowired
  @Qualifier("startDtHistorical")
  private Property<ZonedDateTime> startDtProperty;
  @Autowired
  @Qualifier("endDtHistorical")
  private Property<ZonedDateTime> endDtProperty;

  @Autowired
  @Qualifier("timeIntervalHistorical")
  private Property<TemporalAmount> timeIntervalProperty;

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.startDtDatePicker.getNode() == null) {
      this.startDtDatePicker.nodeProperty().addListener((obs, old, change) -> {
        this.initStartDtPicker();
      });
    } else {
      this.initStartDtPicker();
    }
    if (this.endDtDatePicker.getNode() == null) {
      this.endDtDatePicker.nodeProperty().addListener((obs, old, change) -> {
        initEndDtPicker();
      });
    } else {
      this.initEndDtPicker();
    }
    if (this.zoneIdComboBox.getNode() == null) {
      this.zoneIdComboBox.nodeProperty().addListener((obs, old, change) -> {
        this.initZoneIdComboBox();
      });
    } else {
      this.initZoneIdComboBox();
    }
    if (this.timeCountSpinner.getNode() == null) {
      this.timeCountSpinner.nodeProperty().addListener((obs, old, change) -> {
        this.initTimeCountSpinner();
      });
    } else {
      this.initTimeCountSpinner();
    }
    if (this.timeUnitCmbBox.getNode() == null) {
      this.timeUnitCmbBox.nodeProperty().addListener((obs, old, change) -> {
        this.initTimeUnitCmbBox();
      });
    } else {
      this.initTimeUnitCmbBox();
    }
  }

  /**
   * *
   *
   */
  private void initTimeUnitCmbBox() {
    this.timeUnitCmbBox.getNode().valueProperty().addListener((obs, old, change) -> {
      if (this.timeCountSpinner.getNode() != null) {
        Integer amount = this.timeCountSpinner.getNode().valueProperty().getValue();
        TemporalUnit unit = this.timeUnitCmbBox.getNode().valueProperty().getValue();
        if (amount != null && unit != null) {
          TemporalAmount timeInterval = Duration.of(amount.longValue(), unit);
          this.timeIntervalProperty.setValue(timeInterval);
        } else {
          this.timeIntervalProperty.setValue(null);
        }
      }

    });
    this.timeUnitCmbBox.getNode().getItems().addAll(ChronoUnit.values());
    this.timeUnitCmbBox.getNode().valueProperty().setValue(ChronoUnit.HOURS);
  }

  /**
   *
   */
  private void initTimeCountSpinner() {
    this.timeCountSpinner.getNode().valueProperty().addListener((obs, old, change) -> {
      if (this.timeUnitCmbBox.getNode() != null) {
        Integer amount = this.timeCountSpinner.getNode().valueProperty().getValue();
        TemporalUnit unit = this.timeUnitCmbBox.getNode().valueProperty().getValue();
        if (amount != null && unit != null) {
          TemporalAmount timeInterval = Duration.of(amount.longValue(), unit);
          this.timeIntervalProperty.setValue(timeInterval);
        } else {
          this.timeIntervalProperty.setValue(null);
        }
      }
    });
    this.timeCountSpinner.getNode().setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60));
    this.timeCountSpinner.getNode().getValueFactory().setValue(1);
  }

  private void initZoneIdComboBox() {
    this.zoneIdComboBox.getNode().getSelectionModel().selectedItemProperty().addListener((obs, old, change) -> {
      Object selectedItem = this.zoneIdComboBox.getNode().getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        String value = String.valueOf(selectedItem);
        ZoneId zoneId = ZoneId.of(value);
        this.zoneIdProperty.setValue(zoneId);
      } else {
        this.zoneIdProperty.setValue(null);
      }
    });

    List<String> availableZoneIds = ZoneId.getAvailableZoneIds().stream().sorted().collect(Collectors.toList());
    this.zoneIdComboBox.getNode().setItems(FXCollections.observableArrayList(availableZoneIds));
    this.zoneIdComboBox.getNode().getSelectionModel().select(ZoneId.of("US/Pacific"));
  }

  private void initEndDtPicker() {
    this.endDtDatePicker.getNode().valueProperty().addListener((obs, old, change) -> {
      if (this.zoneIdProperty.getValue() != null) {
        LocalDate localDate = this.endDtDatePicker.getNode().getValue();
        ZonedDateTime newDateTime = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, this.zoneIdProperty.getValue());
        this.endDtProperty.setValue(newDateTime);
      }
    });
    Platform.runLater(() -> {
      this.endDtDatePicker.getNode().valueProperty().setValue(LocalDate.now());
    });

  }

  private void initStartDtPicker() {
    this.startDtDatePicker.getNode().valueProperty().addListener((obs, old, change) -> {
      if (this.zoneIdProperty.getValue() != null) {
        LocalDate localDate = this.startDtDatePicker.getNode().getValue();
        ZonedDateTime newDateTime = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, this.zoneIdProperty.getValue());
        this.startDtProperty.setValue(newDateTime);
      }
    });
    Platform.runLater(() -> {
      this.startDtDatePicker.getNode().valueProperty().setValue(LocalDate.now().minusDays(20));
    });
  }

}
