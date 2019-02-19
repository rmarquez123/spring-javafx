package com.windsim.wpls.view.realtime;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.components.ChildNodeWrapper;
import com.rm.springjavafx.popup.Popup;
import common.types.TimeUtils;
import gov.inl.glass3.calculators.ieee.IeeeDelegates;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.frcstdlr.FcstConfigurations;
import gov.inl.glass3.frcstdlr.FrcstDlrCalculator;
import gov.inl.glass3.frcstdlr.FrcstDlrController;
import gov.inl.glass3.frcstdlr.GroupFcstDlrCalculator;
import gov.inl.glass3.frcstdlr.RealTimeFcstScheduler;
import gov.inl.glass3.frcstdlr.SeriesData;
import gov.inl.glass3.frcstdlr.impl.movingave.DlrMvngAveFrcstDlrCalculator;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import org.controlsfx.control.ToggleSwitch;
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
public class RealTimeController implements InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;
  @Autowired
  RealTimeFcstOptionsController realTimeFcstOptionsController;

  @Autowired
  @Qualifier("realTimeFcstOptions")
  Popup realTimeFcstOptionsPopup;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "zoneId")
  private Property<ZoneId> zoneIdProperty;

  @Autowired
  private RealTimeFcstExporter realTimeFcstExporter;

  @Autowired
  @Qualifier("realTimeSwitch")
  ChildNodeWrapper<ToggleSwitch> realTimeSwitch;

  @Autowired
  @Qualifier("fcstTimeSteps")
  private IntegerProperty fcstTimeStepsProperty;

  @Autowired
  @Qualifier("repeatFcstTimeSteps")
  private IntegerProperty repeatFcstTimeStepsProperty;

  @Autowired
  @Qualifier("timeIntervalHistorical")
  private Property<TemporalAmount> timeIntervalProperty;

  private FrcstDlrController controller;
  private RealTimeFcstScheduler scheduler;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      this.modelProperty.addListener((obs, old, change) -> {
        updateRealTimeMode();
      });
      this.realTimeSwitch.getNode().selectedProperty().addListener((obs, old, change) -> {
        if (change) {
          this.realTimeFcstOptionsController.setOnSaveAction(() -> {
            updateRealTimeMode();
          });
          this.realTimeFcstOptionsPopup.show();
        }
      });
    });
  }

  private void updateRealTimeMode() {
    Model model = this.modelProperty.getValue();
    if (model != null && this.realTimeSwitch.getNode().selectedProperty().getValue()) {

      FrcstDlrCalculator fcstCalc = createFrcstCalculator();
      TemporalAmount timeInterval = this.timeIntervalProperty.getValue();
      ZoneId zoneId = this.zoneIdProperty.getValue();
      ZonedDateTime currentDate = TimeUtils.truncate(ZonedDateTime.ofInstant(Instant.now(), zoneId), timeInterval);
      FcstConfigurations configs = new FcstConfigurations.Builder()
        .setDateTime(currentDate)
        .setForecastHorizonSteps(this.fcstTimeStepsProperty.getValue())
        .setRepeatHorizonSteps(this.repeatFcstTimeStepsProperty.getValue())
        .setTimeInterval(timeInterval)
        .build();
      this.controller = new FrcstDlrController(configs, fcstCalc);
      SeriesData seriesData = new SeriesDataStub(configs);
      if (this.scheduler != null) {
        this.scheduler.stop();
        this.scheduler = null;
      }
      this.scheduler = new RealTimeFcstScheduler();
      Map<String, Measure<Temperature>> maxTemps = new HashMap<>();
      for (ModelPoint modelPoint : model.getModelPoints()) {
        LineSection lineSection = model.getLineSectionByModelPointId(modelPoint.getModelPointId());
        maxTemps.put(modelPoint.getModelPointId(), lineSection.getMaxTemperature());
      }
      this.controller.runFcst(maxTemps, seriesData, this.scheduler, this.realTimeFcstExporter);
    } else {
      if (this.scheduler != null) {
        this.scheduler.stop();
        this.scheduler = null;
      }
    }
  }

  /**
   *
   * @param lineSection
   * @return
   */
  private FrcstDlrCalculator createFrcstCalculator() {
    Model model = this.modelProperty.getValue();
    FrcstDlrCalculator result = new GroupFcstDlrCalculator((modelPointId) -> {
      LineSection lineSection = model.getLineSectionByModelPointId(modelPointId);
      Conductor conductor = model.getConductorByModelPointId(modelPointId);
      IeeeDelegates ieeeDelegates = new IeeeDelegates(lineSection);
      ModelPointGeometry geometry = model.getModelPoints().get(modelPointId).getGeometry();
      DlrMvngAveFrcstDlrCalculator calculator = new DlrMvngAveFrcstDlrCalculator(conductor, geometry, ieeeDelegates);
      return calculator;
    });
    return result;
  }

}
